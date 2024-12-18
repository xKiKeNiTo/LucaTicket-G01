package com.grupo01.spring.config;

import com.grupo01.spring.model.HistoricoVentas;
import com.grupo01.spring.model.EventResponse;
import com.grupo01.spring.repository.CompraRepository;
import com.grupo01.spring.repository.HistoricoVentasRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.UUID;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	private final CompraRepository compraRepository;
	private final HistoricoVentasRepository historicoVentasRepository;

	public BatchConfig(CompraRepository compraRepository, HistoricoVentasRepository historicoVentasRepository) {
		this.compraRepository = compraRepository;
		this.historicoVentasRepository = historicoVentasRepository;
	}

	@Bean
	ItemReader<UUID> reader() {
		return new ItemReader<>() {
			private Iterator<UUID> iterator;

			@Override
			public UUID read() {
				// Inicializa el iterator la primera vez que se llama a read()
				if (iterator == null) {
					iterator = compraRepository.findDistinctIdEventos().iterator();
				}
				// Devuelve el siguiente elemento, si existe
				return iterator.hasNext() ? iterator.next() : null;
			}
		};
	}

	@Bean
	ItemProcessor<UUID, HistoricoVentas> processor() {
		return idEvento -> {
			BigDecimal precioMedio = compraRepository.calcularPrecioPromedioPorEvento(idEvento);
			BigDecimal precioMaximo = compraRepository.findMaxPrecioPorEvento(idEvento);
			BigDecimal precioMinimo = compraRepository.findMinPrecioPorEvento(idEvento);
			long conteoVentas = compraRepository.contarComprasPorEvento(idEvento);

			if (precioMedio == null) {
				return null;
			}

			HistoricoVentas historico = historicoVentasRepository.findByIdEvento(idEvento);
			if (historico == null) {
				historico = new HistoricoVentas();
				historico.setIdEvento(idEvento);
			}

			historico.setPrecioMedio(precioMedio);
			historico.setPrecioMaximo(precioMaximo);
			historico.setPrecioMinimo(precioMinimo);
			historico.setConteoVentas(conteoVentas);
			historico.setTimestamp(LocalDateTime.now());
			return historico;
		};
	}

	@Bean
	ItemWriter<HistoricoVentas> writer() {
		return items -> historicoVentasRepository.saveAll(items);
	}

	@Bean
	Step stepVentaHistorico(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			ItemReader<UUID> reader, ItemProcessor<UUID, HistoricoVentas> processor,
			ItemWriter<HistoricoVentas> writer) {
		return new StepBuilder("stepVentaHistorico", jobRepository).<UUID, HistoricoVentas>chunk(10, transactionManager)
				.reader(reader).processor(processor).writer(writer).build();
	}

	@Bean
	Job jobVentaHistorico(JobRepository jobRepository, Step stepVentaHistorico) {
		return new JobBuilder("jobVentaHistorico", jobRepository).start(stepVentaHistorico).build();
	}

	@Bean
	ItemProcessor<HistoricoVentas, EventResponse> actualizarEventosProcessor() {
		return historico -> {
			BigDecimal precioMedio = historico.getPrecioMedio();
			BigDecimal precioMinimo = precioMedio.multiply(BigDecimal.valueOf(0.8));
			BigDecimal precioMaximo = precioMedio.multiply(BigDecimal.valueOf(1.2));

			// Aquí debes llamar al cliente que actualiza los precios en la base de datos
			// del evento
			// eventClient.actualizarPrecios(historico.getIdEvento(), precioMinimo,
			// precioMaximo);

			return new EventResponse(historico.getIdEvento().toString(), precioMinimo, precioMaximo);
		};
	}

	@Bean
	Step stepActualizarEventos(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			ItemReader<HistoricoVentas> reader, ItemProcessor<HistoricoVentas, EventResponse> processor) {
		return new StepBuilder("stepActualizarEventos", jobRepository)
				.<HistoricoVentas, EventResponse>chunk(10, transactionManager).reader(reader).processor(processor)
				.build();
	}

	@Bean
	Job jobActualizarEventos(JobRepository jobRepository, Step stepActualizarEventos) {
		return new JobBuilder("jobActualizarEventos", jobRepository).start(stepActualizarEventos).build();
	}
}
