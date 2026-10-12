# Relatório de Correções do Projeto SIGA - Etapa 1


## 1. Cadastro incompleto e repetição de alunos
 
 * O sistema de armazenamento provisório (a classe `AlunoDAOMemoria`) estava incompleto. Ele permitia cadastrar dois alunos com o mesmo número de matrícula e não tinha o código necessário para atualizar as informações de um aluno já existente. Além disso, a classe de serviço, que faz o meio de campo com a tela, não tinha os comandos para consultar, alterar e excluir.


* No armazenamento, criei uma verificação: antes de salvar um aluno novo, o sistema busca para ver se a matrícula já existe. Se existir, ele barra o cadastro.
* Escrevi o código para a atualização: o sistema agora procura a posição exata do aluno antigo na lista e o substitui pelas novas informações.
* Na classe de serviço, criei os métodos que faltavam para que a tela pudesse finalmente conversar com o armazenamento e realizar todas as operações do cadastro.

---

## 2. Coleção Exposta
 
* Quando a tela pedia para listar os alunos, o sistema estava entregando a sua lista original de bandeja. O problema disso é que a tela (ou qualquer outra parte do código) conseguia adicionar um "aluno intruso" diretamente nessa lista, pulando todas as nossas regras de validação.

* Fiz com que o armazenamento passasse a entregar apenas uma **cópia** (um clone) da lista de alunos. Assim, se a tela tentar adicionar ou apagar alguém dessa lista recebida, ela estará mexendo apenas na sua própria cópia, mantendo a nossa lista oficial totalmente segura e protegida.

---

## 3. Exclusão Silenciosa

* Ao pedir para o sistema excluir uma matrícula que não existia (por exemplo, "0000000"), ele procurava, não achava nada e simplesmente ficava quieto. O usuário na tela recebia uma mensagem de sucesso, achando que a exclusão tinha dado certo, quando na verdade o aluno nem existia. Falhar em silêncio é muito perigoso.

* Adicionei uma checagem simples antes de excluir. Agora, o sistema tenta buscar o aluno primeiro; se o resultado for vazio, ele dispara um erro claro avisando que a matrícula não existe e que ninguém foi removido.

---

## 4. Validação Duplicada
 
* A regra que diz qual é a nota máxima permitida estava escrita em dois lugares diferentes: na tela principal (a classe `Main`) e na classe de serviço. Para piorar, elas estavam diferentes! A tela aceitava notas até 100, mas o serviço só aceitava até 10. Ter a mesma regra em dois lugares gera confusão e, com o tempo, elas sempre acabam divergindo.

* Apaguei totalmente a verificação de notas que ficava na tela. A tela não tem que tomar decisões de negócio. Depois, centralizei todas as regras (de nome em branco e limite de nota) em um único método privado dentro da classe de serviço. Agora, a tela apenas manda os dados e espera o serviço dizer se aprovou ou se deu erro, mantendo as regras do sistema em um único lugar seguro.
