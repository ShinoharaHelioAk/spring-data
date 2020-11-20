package br.com.alura.spring.data.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.alura.spring.data.orm.Funcionario;
import br.com.alura.spring.data.projecoes.FuncionarioProjecao;
import br.com.alura.spring.data.repository.FuncionarioRepository;

@Service
public class RelatoriosService {
	private Boolean system = true;
	private FuncionarioRepository funcionarioRepository;
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	public RelatoriosService(FuncionarioRepository funcionarioRepository) {
		this.funcionarioRepository = funcionarioRepository;
	}
	
	public void inicial(Scanner scanner) {
		system = true;
		while(system) {
			System.out.println("Qual ação de Relatório deseja executar?");
			System.out.println("0 - Sair");
			System.out.println("1 - Busca Funcionário por Nome");
			System.out.println("2 - Busca Funcionário por Nome, Data de Contratação e Salário Maior");
			System.out.println("3 - Busca Funcionário por Data de Contratação Maior");
			System.out.println("4 - Pesquisa Funcionário Salário");
			
			int acao = scanner.nextInt();
			
			switch (acao) {
			case 1:
				buscaFuncionarioPorNome(scanner);
				break;
			case 2:
				buscaFuncionarioPorNomeSalarioMaiorData(scanner);
				break;
			case 3:
				buscaFuncionarioPorDataContratacaoMaior(scanner);
				break;
			case 4:
				pesquisaFuncionarioSalario();
				break;
			default:
				system = false;
				break;
			}
		}
	}
	
	private void buscaFuncionarioPorNome(Scanner scanner) {
		System.out.println("Qual nome deseja pesquisar?");
		String nome = scanner.next();
		
		//List<Funcionario> funcionarios = funcionarioRepository.findByNome(nome);
		//List<Funcionario> funcionarios = funcionarioRepository.findByNomeLike(nome);
		System.out.println("Qual página deseja visualizar?");
		Integer page = scanner.nextInt();
		int quantidadeRegistrosPorPagina = 5;
		Pageable pageable = PageRequest.of(page, quantidadeRegistrosPorPagina, Sort.unsorted());
		Page<Funcionario> funcionarios = funcionarioRepository.findByNomeLike(nome, pageable);
		
		System.out.println("----------------------------");
		
		//Quantidade de Paginações: Page 1 of 4 containing br.com.alura.spring.data.orm.Funcionario instances
		//Quantidade de Paginações: Page 2 of 4 containing br.com.alura.spring.data.orm.Funcionario instances
		//Quantidade de Paginações: Page 3 of 4 containing br.com.alura.spring.data.orm.Funcionario instances
		//Quantidade de Paginações: Page 4 of 4 containing br.com.alura.spring.data.orm.Funcionario instances
		System.out.println("Quantidade de Paginações: " + funcionarios);
		System.out.println("Quantidade de registros retornados por página: " + quantidadeRegistrosPorPagina);
		System.out.println("Quantidade de registros retornados nesta página: " + funcionarios.getNumberOfElements());
		System.out.println("Total de Registros retornados nesta consulta: " + funcionarios.getTotalElements());
		
		System.out.println("----------------------------");
		funcionarios.forEach(System.out::println);
		System.out.println("----------------------------");
	}
	
	private void buscaFuncionarioPorNomeSalarioMaiorData(Scanner scanner) {
		System.out.println("Qual nome deseja pesquisar?");
		String nome = scanner.next();
		
		System.out.println("Qual data de contratação deseja pesquisar?");
		String data = scanner.next();
		LocalDate dataContratacao = LocalDate.parse(data, formatter);
		
		System.out.println("Digite o mínimo do salário que deseja pesquisar:");
		Double salario = scanner.nextDouble();
		
		List<Funcionario> listaFuncionarios = funcionarioRepository.findNomeSalarioMaiorDataContratacao(nome, salario, dataContratacao);
		System.out.println("----------------------------");
		listaFuncionarios.forEach(System.out::println);
		System.out.println("----------------------------");
	}
	
	private void buscaFuncionarioPorDataContratacaoMaior(Scanner scanner) {
		System.out.println("Qual data mímina de contratação que deseja pesquisar?");
		String data = scanner.next();
		LocalDate dataContratacao = LocalDate.parse(data, formatter);
		
		List<Funcionario> listaFuncionarios = funcionarioRepository.findDataContratacaoMaior(dataContratacao);
		System.out.println("----------------------------");
		listaFuncionarios.forEach(System.out::println);
		System.out.println("----------------------------");
	}
	
	private void pesquisaFuncionarioSalario() {
		List<FuncionarioProjecao> list = funcionarioRepository.findFuncionarioSalario();
		System.out.println("----------------------------");
		list.forEach(f->System.out.println("Funcionario [ID: "+f.getId()+" | Nome: "+f.getNome()+" | Salário: "+f.getSalario()+"]"));
		System.out.println("----------------------------");
	}
}
