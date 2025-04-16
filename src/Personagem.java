public class Personagem {
    private static int contador = 0;
    private int idPersonagem;
    public String nome;
    public int nivel;
    public int vidaMaxima, vidaAtual;
    public int manaMaxima, manaAtual;

    public ListaHabilidade habilidades;

    public Personagem(String nome) {
        if (nome == null || nome.isEmpty()) {
            System.out.println("O nome do personagem não pode ser vazio");
            nome = "Personagem " + (++contador);
        }
        this.idPersonagem = ++contador;
        this.nome = nome;
        this.nivel = 1;
        this.vidaMaxima = 100;
        this.vidaAtual = 100;
        this.manaMaxima = 50;
        this.manaAtual = 50;
        this.habilidades = new ListaHabilidade();
    }

    public void receberDano(int valor) {
        if (valor < 0) {
            System.out.println("Dano negativo? Tá de sacanagem");
            return;
        }
        vidaAtual -= valor;
        if (vidaAtual < 0) vidaAtual = 0;
        System.out.println(nome + " recebeu " + valor + " de dano");
        System.out.println("Vida atual: " + vidaAtual);
    }

    public void curar(int valor) {
        if (valor < 0) {
            System.out.println("O valor de cura não pode ser negativo");
            return;
        }
        vidaAtual += valor;
        if (vidaAtual > vidaMaxima) vidaAtual = vidaMaxima;
        System.out.println(nome + " foi curado em " + valor + ". Vida atual: " + vidaAtual);
    }

    public boolean estaVivo() {
        return vidaAtual > 0;
    }

    public void subirNivel() {
        nivel++;
        vidaMaxima += 10;
        manaMaxima += 5;
        vidaAtual = vidaMaxima;
        manaAtual = manaMaxima;
        System.out.println(nome + " subiu para o nível " + nivel + "! Vida máxima: " + vidaMaxima + ", Mana máxima: " + manaMaxima);
    }

    public void usarHabilidade(int idHabilidade, Personagem alvo) {
        if (alvo == null) {
            System.out.println("O alvo não pode ser nulo");
            return;
        }

        Habilidade habilidade = habilidades.buscarPorId(idHabilidade);
        if (habilidade == null) {
            System.out.println("Habilidade com ID " + idHabilidade + " não encontrada");
            return;
        }

        if (manaAtual < habilidade.custoMana) {
            System.out.println("Mana insuficiente para usar a habilidade " + habilidade.nome);
            return;
        }

        alvo.receberDano(habilidade.dano);
        manaAtual -= habilidade.custoMana;
        System.out.println(nome + " usou a habilidade " + habilidade.nome + " em " + alvo.nome + ". Mana restante: " + manaAtual);
    }

    public void buscarPersonagemPorId(int id) {
        if (id == this.idPersonagem) {
            System.out.println("Personagem encontrado: " + nome);
        } else {
            System.out.println("Personagem não encontrado");
        }
    }

    public int getIdPersonagem() {
        return idPersonagem;
    }
}