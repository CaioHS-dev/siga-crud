package siga;

import java.util.ArrayList;
import java.util.List;

/**
 * Código INICIAL da atividade — CRUD INCOMPLETO e com deslizes PROPOSITAIS.
 *
 * DESLIZE 1 — coleção interna exposta (etapa 3):
 * listarTodos() devolve a PRÓPRIA lista interna. Quem chamar o método pode
 * adicionar ou remover alunos diretamente, por fora do DAO, quebrando o
 * encapsulamento estudado na Aula 1. A correção é a cópia defensiva:
 * devolver uma nova lista com os mesmos elementos.
 *
 * DESLIZE 2 — exclusão silenciosa (etapa 3):
 * remover() não verifica se o aluno existia. Quando a matrícula não é
 * encontrada, nada acontece — mas o sistema informa sucesso ao usuário.
 * Falhar em silêncio é pior que falhar com erro.
 *
 * PENDENTE (etapa 1): o método atualizar() ainda não foi implementado, e
 * inserir() não impede matrícula duplicada.
 *
 * Tarefa:
 *   - Etapa 1: completar as quatro operações do CRUD;
 *   - Etapa 3: corrigir a coleção exposta e a exclusão silenciosa.
 */
public class AlunoDAOMemoria implements AlunoDAO {

    private final List<Aluno> armazem = new ArrayList<>();

    @Override
    public void inserir(Aluno aluno) {
        // Correção da Etapa 1: Impede matrícula duplicada
        if (buscarPorMatricula(aluno.getMatricula()) != null) {
            throw new IllegalStateException("Já existe um aluno com a matrícula " + aluno.getMatricula());
        }
        armazem.add(aluno);
    }

    @Override
    public Aluno buscarPorMatricula(String matricula) {
        for (Aluno aluno : armazem) {
            if (aluno.getMatricula().equals(matricula)) {
                return aluno;
            }
        }
        return null;   // não encontrado
    }

    @Override
    public List<Aluno> listarTodos() {
        // Correção da Etapa 3: devolve uma cópia defensiva da coleção interna.
        return new ArrayList<>(armazem);
    }

    @Override
    public void atualizar(Aluno aluno) {
        // Correção da Etapa 1: Implementação da atualização com verificação
        Aluno existente = buscarPorMatricula(aluno.getMatricula());
        if (existente == null) {
            throw new IllegalArgumentException("Aluno não encontrado para atualização.");
        }
        int index = armazem.indexOf(existente);
        armazem.set(index, aluno);
    }

    @Override
    public void remover(String matricula) {
        // Correção do Deslize 2: Verificação de existência para evitar falha silenciosa
        Aluno encontrado = buscarPorMatricula(matricula);
        if (encontrado == null) {
            throw new IllegalArgumentException("Matrícula inexistente. Nenhum aluno foi removido.");
        }
        armazem.remove(encontrado);
    }
}
