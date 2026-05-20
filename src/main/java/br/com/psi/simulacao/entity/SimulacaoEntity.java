package br.com.psi.simulacao.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "simulacoes")
public class SimulacaoEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, precision = 19, scale = 2)
    public BigDecimal valorInicial;

    @Column(nullable = false, precision = 10, scale = 4)
    public BigDecimal taxaJurosMensal;

    @Column(nullable = false)
    public Integer prazoMeses;

    @Column(nullable = false, precision = 19, scale = 2)
    public BigDecimal valorTotalFinal;

    @Column(nullable = false, precision = 19, scale = 2)
    public BigDecimal valorTotalJuros;

    @Column(nullable = false)
    public OffsetDateTime criadoEm;

    @OneToMany(mappedBy = "simulacao", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<MemoriaCalculoEntity> memoriaCalculo = new ArrayList<>();
}
