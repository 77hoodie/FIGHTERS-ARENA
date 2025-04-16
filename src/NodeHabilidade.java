class NodeHabilidade {
    Habilidade habilidade;
    NodeHabilidade proximo;

    public NodeHabilidade(Habilidade h) {
        habilidade = h;
    }
}

class ListaHabilidade {
    NodeHabilidade inicio;

    public void adicionar(Habilidade h) {
        NodeHabilidade novo = new NodeHabilidade(h);
        if (inicio == null) {
            inicio = novo;
        } else {
            NodeHabilidade atual = inicio;
            while (atual.proximo != null) {
                atual = atual.proximo;
            }
            atual.proximo = novo;
        }
    }

    Habilidade buscarPorId(int id) {
        NodeHabilidade atual = inicio;
        while (atual != null) {
            if (atual.habilidade.id == id) {
                return atual.habilidade;
            }
            atual = atual.proximo;
        }
        return null;
    }
}
