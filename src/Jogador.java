public class Jogador {
    private int contador = 0;
    private int idJogador;
    private String nome;
    private String senha;
    public int saldoMoedas;

    public ListaPersonagem personagens;

    public Jogador(String nome, String senha) {
        if (nome == null || nome.isEmpty()) {
            System.out.println("O nome não pode ser vazio");
        }
        if (senha == null || senha.isEmpty()) {
            System.out.println("A senha não pode ser vazia");
        }

        this.idJogador = ++contador;
        this.nome = nome;
        this.senha = senha;
        this.saldoMoedas = 0;
        this.personagens = new ListaPersonagem();
    }

    public boolean autenticar(String senhaDigitada) {
        return this.senha.equals(senhaDigitada);
    }

    public void criarPersonagem(String nomePersonagem) {
        if (nomePersonagem == null || nomePersonagem.isEmpty()) {
            System.out.println("O nome do personagem não pode ser vazio");
        }
        personagens.adicionar(new Personagem(nomePersonagem));
    }

    public Personagem selecionarPersonagem(String nomePersonagem) {
        return personagens.buscar(nomePersonagem);
    }

    public int getIdJogador() {
        return idJogador;
    }

    public String getNome() {
        return nome;
    }

    public int getSaldoMoedas() {
        return saldoMoedas;
    }

    @Override
    public String toString() {
        return "Jogador{" +
                "idJogador=" + idJogador +
                ", nome='" + nome + '\'' +
                ", saldoMoedas=" + saldoMoedas +
                ", personagens=" + personagens +
                '}';
    }
}