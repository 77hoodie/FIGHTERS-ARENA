class NodePersonagem {
    Personagem personagem;
    NodePersonagem proximo;

    public NodePersonagem(Personagem p) {
        personagem = p;
    }
            
}

class ListaPersonagem {
    NodePersonagem inicio;

    public void adicionar(Personagem p) {
        NodePersonagem novo = new NodePersonagem(p);
        if (inicio == null) {
            inicio = novo;
        } else {
            NodePersonagem atual = inicio;
            while (atual.proximo != null) {
                atual = atual.proximo;
            }
            atual.proximo = novo;
        }
    }

    Personagem buscar(String nome) {
        NodePersonagem atual = inicio;
        while (atual != null) {
            if (atual.personagem.nome.equalsIgnoreCase(nome)) {
                return atual.personagem;
            }
            atual = atual.proximo;
        }
        return null;
    }
}
