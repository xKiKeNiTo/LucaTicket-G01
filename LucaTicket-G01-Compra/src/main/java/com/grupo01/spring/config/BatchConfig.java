package com.grupo01.spring.config;

import com.grupo01.spring.model.HistoricoVentas;
import com.grupo01.spring.feignClient.EventClient;
import com.grupo01.spring.model.EventResponse;
import com.grupo01.spring.repository.CompraRepository;
import com.grupo01.spring.repository.HistoricoVentasRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
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
public class BatchConfig {

	private final EventClient eventClient;
	private final CompraRepository compraRepository;
	private final HistoricoVentasRepository historicoVentasRepository;

	public BatchConfig(CompraRepository compraRepository, HistoricoVentasRepository historicoVentasRepository,
			EventClient eventClient) {
		this.eventClient = eventClient;
		this.compraRepository = compraRepository;
		this.historicoVentasRepository = historicoVentasRepository;
	}

	@Bean
	ItemReader<UUID> readerUUID() {
		return new ItemReader<>() {
			private Iterator<UUID> iterator;

			@Override
			public UUID read() {
				if (iterator == null) {
					iterator = compraRepository.findDistinctIdEventos().iterator();
				}
				return iterator.hasNext() ? iterator.next() : null;
			}
		};
	}

	@Bean
	ItemReader<HistoricoVentas> readerHistoricoVentas() {
		return new ItemReader<>() {
			private Iterator<HistoricoVentas> iterator;

			@Override
			public HistoricoVentas read() {
				if (iterator == null) {
					iterator = historicoVentasRepository.findAll().iterator();
				}
				return iterator.hasNext() ? iterator.next() : null;
			}
		};
	}

	@Bean
	ItemProcessor<UUID, HistoricoVentas> processorVentaHistorico() {
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
	ItemWriter<HistoricoVentas> writerVentaHistorico() {
		return items -> historicoVentasRepository.saveAll(items);
	}

	@Bean
	Step stepVentaHistorico(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			ItemReader<UUID> readerUUID, ItemProcessor<UUID, HistoricoVentas> processor,
			ItemWriter<HistoricoVentas> writer) {
		return new StepBuilder("stepVentaHistorico", jobRepository).<UUID, HistoricoVentas>chunk(10, transactionManager)
				.reader(readerUUID).processor(processor).writer(writer).build();
	}

	@Bean
	Job jobVentaHistorico(JobRepository jobRepository, Step stepVentaHistorico) {
		return new JobBuilder("jobVentaHistorico", jobRepository).start(stepVentaHistorico).build();
	}

	@Bean
	ItemProcessor<HistoricoVentas, EventResponse> processorActualizarEventos() {
		return historico -> {
			BigDecimal precioMedio = historico.getPrecioMedio();
			BigDecimal precioMinimo = precioMedio.multiply(BigDecimal.valueOf(0.8));
			BigDecimal precioMaximo = precioMedio.multiply(BigDecimal.valueOf(1.2));

			return new EventResponse(historico.getIdEvento(), precioMinimo, precioMaximo);
		};
	}

	@Bean
	ItemWriter<EventResponse> writerActualizarEventos() {
		return items -> {
			for (EventResponse response : items) {
				try {
					// Llama al método del cliente REST para actualizar precios
					eventClient.actualizarPrecios(response.getIdEvento(), response.getPrecioMinimo(),
							response.getPrecioMaximo());
					System.out.println("Evento actualizado exitosamente: " + response.getIdEvento());
				} catch (Exception e) {
					System.err.println("Error actualizando evento: " + response.getIdEvento());
					e.printStackTrace();
				}
			}
		};
	}

	@Bean
	Step stepActualizarEventos(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			ItemReader<HistoricoVentas> readerHistoricoVentas, ItemProcessor<HistoricoVentas, EventResponse> processor,
			ItemWriter<EventResponse> writer) {
		return new StepBuilder("stepActualizarEventos", jobRepository)
				.<HistoricoVentas, EventResponse>chunk(10, transactionManager).reader(readerHistoricoVentas)
				.processor(processor).writer(writer).build();
	}

	@Bean
	Job jobActualizarEventos(JobRepository jobRepository, Step stepActualizarEventos) {
		return new JobBuilder("jobActualizarEventos", jobRepository).start(stepActualizarEventos).build();
	}

}
