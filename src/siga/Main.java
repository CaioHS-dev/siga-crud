package siga;

import java.util.List;

/**
 * Ponto de entrada do SIGA (código INICIAL da atividade da Aula 8).
 *
 * Esta é a camada de APRESENTAÇÃO. Ela demonstra, em execução, os três
 * deslizes que você deverá corrigir — e contém, ela própria, um deles.
 *
 * DESLIZE 3 (aqui): a validação da média está repetida nesta camada, com um
 * limite DIFERENTE do usado no ServicoAluno (aqui aceita até 100). Regra de
 * domínio duplicada em dois lugares acaba divergindo, como se vê.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== SIGA - Atividade CRUD e Etapa 1 (Código Refatorado) ===\n");

        AlunoDAO dao = new AlunoDAOMemoria();
        ServicoAluno servico = new ServicoAluno(dao);

        // --- CREATE ---
        cadastrar(servico, new Aluno("Maria Silva", "2026001", 8.5));
        cadastrar(servico, new Aluno("João Souza",  "2026002", 6.0));
        System.out.println();

        // --- READ ---
        System.out.println("Alunos cadastrados:");
        for (Aluno aluno : servico.listar()) {
            System.out.println("  " + aluno);
        }

        // Testando a correção do Deslize 1 (Cópia defensiva)
        List<Aluno> lista = servico.listar();
        lista.add(new Aluno("Intruso Silva", "9999999", 10));
        System.out.println("\nApós a tela tentar inserir um aluno DIRETAMENTE na lista:");
        for (Aluno aluno : servico.listar()) {
            System.out.println("  " + aluno);
        }
        System.out.println("  (O 'Intruso' não entrou, pois a coleção está protegida pelo encapsulamento)");

        // Testando a correção do Deslize 2 (Exclusão silenciosa impedida)
        System.out.println("\nTentando excluir matrícula inexistente:");
        try {
            servico.excluir("0000000");
        } catch (IllegalArgumentException e) {
            System.out.println("  Recusado: " + e.getMessage());
        }

        // Correção do Deslize 3: Validação divergente removida. 
        // A tela agora apenas confia e repassa para o serviço.
        Aluno suspeito = new Aluno("Média Absurda", "2026003", 50);
        System.out.println("\nTentando cadastrar aluno com média 50:");
        try {
            servico.cadastrar(suspeito);
        } catch (IllegalArgumentException e) {
            System.out.println("  Recusado pelo serviço: " + e.getMessage());
            System.out.println("  (A regra de validação agora mora em um único lugar)");
        }

        // --- UPDATE ---
        System.out.println("\nTestando a atualização de um aluno:");
        try {
            servico.alterar(new Aluno("Maria Silva", "2026001", 9.5));
            System.out.println("  Aluno 2026001 atualizado com sucesso.");
            System.out.println("  Dados novos: " + servico.consultar("2026001"));
        } catch (IllegalArgumentException e) {
            System.out.println("  Falha ao atualizar: " + e.getMessage());
        }
    }

    /** Apresentação: traduz as exceções do serviço em mensagens ao usuário. */
    private static void cadastrar(ServicoAluno servico, Aluno aluno) {
        try {
            servico.cadastrar(aluno);
            System.out.println("Cadastrado: " + aluno);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Não foi possível cadastrar: " + e.getMessage());
        }
    }
}
