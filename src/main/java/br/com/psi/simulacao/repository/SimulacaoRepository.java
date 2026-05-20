package br.com.psi.simulacao.repository;

import br.com.psi.simulacao.entity.SimulacaoEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SimulacaoRepository implements PanacheRepository<SimulacaoEntity> {
}
