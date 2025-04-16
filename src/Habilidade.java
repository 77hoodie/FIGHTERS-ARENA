public class Habilidade {
    public int id;
    public String nome;
    public int custoMana;
    public int dano;

    public Habilidade(int id, String nome, int custoMana, int dano) {
        this.id = id;
        this.nome = nome;
        this.custoMana = custoMana;
        this.dano = dano;
    }

    public void usar(Personagem usuario, Personagem alvo) {
        alvo.receberDano(dano);
        usuario.manaAtual -= custoMana;
        System.out.println(usuario.nome + " usou " + nome + " em " + alvo.nome + " causando " + dano + " de dano.");
    }
}
