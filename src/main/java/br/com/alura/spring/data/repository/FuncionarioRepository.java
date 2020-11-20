package br.com.alura.spring.data.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import br.com.alura.spring.data.orm.Funcionario;
import br.com.alura.spring.data.projecoes.FuncionarioProjecao;

//public interface FuncionarioRepository extends CrudRepository<Funcionario, Integer> {}
@Repository
public interface FuncionarioRepository extends PagingAndSortingRepository<Funcionario, Integer>, JpaSpecificationExecutor<Funcionario> {
	//JpaSpecificationExecutor<Funcionario> --> Permite realizar queries dinâmicas, equivalente à biblioteca de Criteria do JPA.
	
	//Derived Queries.
	List<Funcionario> findByNome(String nome);
	Page<Funcionario> findByNomeLike(String nome, Pageable pageable);
	List<Funcionario> findByNomeLike(String nome);

	//List<Funcionario> findByNomeAndSalarioGreaterThanAndDataContratacao(String nome, Double salario, LocalDate data);
	//Nas 2 linhas abaixo, usando JPQL.
	@Query("select f from Funcionario f where f.nome = :nome and f.salario >= :salario and f.dataContratacao = :data")
	List<Funcionario> findNomeSalarioMaiorDataContratacao(String nome, Double salario, LocalDate data);
	
	//Nas 2 linhas abaixo, usando Native Queries.
	@Query(value = "select * from Funcionarios f where data_contratacao >= :data", nativeQuery = true)
	List<Funcionario> findDataContratacaoMaior(LocalDate data);
	
	//Usando Projection com Native Query.
	@Query(value = "SELECT f.id, f.nome, f.salario FROM funcionarios f", nativeQuery = true)
	List<FuncionarioProjecao> findFuncionarioSalario();
}
