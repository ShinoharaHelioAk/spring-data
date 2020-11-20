package br.com.alura.spring.data.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.alura.spring.data.orm.Cargo;
import br.com.alura.spring.data.orm.Funcionario;
import br.com.alura.spring.data.orm.UnidadeTrabalho;
import br.com.alura.spring.data.repository.CargoRepository;
import br.com.alura.spring.data.repository.FuncionarioRepository;
import br.com.alura.spring.data.repository.UnidadeTrabalhoRepository;

@Service
public class CrudFuncionarioService {
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	private final CargoRepository cargoRepository;
	private final FuncionarioRepository funcionarioRepository;
	private final UnidadeTrabalhoRepository unidadeTrabalhoRepository;
	
	private Boolean system = true;
	
	public CrudFuncionarioService(FuncionarioRepository funcionarioRepository,
			CargoRepository cargoRepository,
			UnidadeTrabalhoRepository unidadeTrabalhoRepository) {
		this.funcionarioRepository = funcionarioRepository;
		this.cargoRepository = cargoRepository;
		this.unidadeTrabalhoRepository = unidadeTrabalhoRepository;
	}
	
	public void inicial(Scanner scanner) {
		system = true;
		while(system) {
			System.out.println("Qual ação de Funcionário deseja executar?");
			System.out.println("0 - Sair");
			System.out.println("1 - Inserir");
			System.out.println("2 - Atualizar");
			System.out.println("3 - Visualizar");
			System.out.println("4 - Deletar");
			
			int opcao = scanner.nextInt();
			
			switch (opcao) {
			case 1:
				salvar(scanner);
				break;
			case 2:
				atualizar(scanner);
				break;
			case 3:
				//visualizar();
				visualizar(scanner);
				break;
			case 4:
				deletar(scanner);
				break;
			default:
				system = false;
				break;
			}
		}
	}
	
	private void salvar(Scanner scanner) {
		System.out.println("Nome");
		String nome = scanner.next();
		
		System.out.println("CPF");
		String cpf = scanner.next();
		
		System.out.println("Salário");
		Double salario = scanner.nextDouble();
		
		System.out.println("Data de Contratação");
		String dataContratacao = scanner.next();
		
		System.out.println("Digite o cargoId");
		Integer cargoId = scanner.nextInt();
		
		List<UnidadeTrabalho> unidades = listaUnidades(scanner);
		
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(nome);
		funcionario.setCpf(cpf);
		funcionario.setSalario(salario);
		funcionario.setDataContratacao(LocalDate.parse(dataContratacao, formatter));
		
		Optional<Cargo> cargo = cargoRepository.findById(cargoId);
		funcionario.setCargo(cargo.get());
		
		funcionario.setUnidadesTrabalho(unidades);
		
		funcionarioRepository.save(funcionario);
		System.out.println("Salvo");
	}

	private List<UnidadeTrabalho> listaUnidades(Scanner scanner) {
		Boolean adicionarUnidade = true;
		List<UnidadeTrabalho> unidades = new ArrayList<UnidadeTrabalho>();
		
		while(adicionarUnidade) {
			System.out.println("Digite o id da unidade para adicionar ao funcionário (unidadeId) -- Para sair, digite 0");
			Integer unidadeId = scanner.nextInt();
			
			if (unidadeId != 0) {
				Optional<UnidadeTrabalho> unidade = unidadeTrabalhoRepository.findById(unidadeId);
				unidades.add(unidade.get());
			} else {
				adicionarUnidade = false;
			}
		}
		
		return unidades;
	}
	
	private void atualizar(Scanner scanner) {
		System.out.println("Nome");
		String nome = scanner.next();
		
		System.out.println("CPF");
		String cpf = scanner.next();
		
		System.out.println("Salário");
		Double salario = scanner.nextDouble();
		
		System.out.println("Data de Contratação");
		String dataContratacao = scanner.next();
		
		System.out.println("Digite o cargoId");
		Integer cargoId = scanner.nextInt();
		
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(nome);
		funcionario.setCpf(cpf);
		funcionario.setSalario(salario);
		funcionario.setDataContratacao(LocalDate.parse(dataContratacao, formatter));
		
		Optional<Cargo> cargo = cargoRepository.findById(cargoId);
		funcionario.setCargo(cargo.get());
		
		funcionarioRepository.save(funcionario);
		System.out.println("Atualizado");
	}
	
	private void visualizar() {
		Iterable<Funcionario> funcionarios = funcionarioRepository.findAll();
		System.out.println("----------------------------");
		funcionarios.forEach(funcionario -> {
			System.out.println(funcionario);
			System.out.println("----------------------------");
		});
	}
	
	private void visualizar(Scanner scanner) {
		System.out.println("Qual página deseja visualizar?");
		Integer page = scanner.nextInt();
		
		int quantidadeRegistrosPorPagina = 5;
		Pageable pageable = PageRequest.of(page, quantidadeRegistrosPorPagina, Sort.unsorted());
		//Pageable pageable = PageRequest.of(page, quantidadeRegistrosPorPagina, Sort.by(Sort.Direction.ASC, "nome"));
		//Pageable pageable = PageRequest.of(page, quantidadeRegistrosPorPagina, Sort.by(Sort.Direction.ASC, "salario"));
		
		//Iterable<Funcionario> funcionarios = funcionarioRepository.findAll();
		Page<Funcionario> funcionarios = funcionarioRepository.findAll(pageable);
		
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
		funcionarios.forEach(funcionario -> {
			System.out.println(funcionario);
			System.out.println("----------------------------");
		});
	}
	
	private void deletar(Scanner scanner) {
		System.out.println("ID");
		int id = scanner.nextInt();
		funcionarioRepository.deleteById(id);
		System.out.println("Deletado");
	}
}
