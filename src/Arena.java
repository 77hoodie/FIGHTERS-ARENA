import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Arena {
    private int idBatalha;
    private Fila listaDeParticipantes;
    public Pilha rankingFinal;
    private int turnoAtual;
    private String estadoBatalha;
    public Personagem[] referencia;
    public boolean fugaOcorreu = false;
    public Personagem fugitivo = null;

    public Arena(int idBatalha, int tamanhoMaximo) {
        this.idBatalha = idBatalha;
        this.listaDeParticipantes = new Fila(tamanhoMaximo);
        this.rankingFinal = new Pilha(tamanhoMaximo);
        this.turnoAtual = 1;
        this.estadoBatalha = "Preparando";
    }

    public void iniciarBatalha(Personagem[] participantes) {
        this.referencia = participantes;
        this.listaDeParticipantes = new Fila(participantes.length);
        this.rankingFinal = new Pilha(participantes.length);
        this.turnoAtual = 1;
        
        for (Personagem personagem : participantes) {
            if (personagem != null && personagem.estaVivo()) {
                listaDeParticipantes.add(personagem.getIdPersonagem());
                System.out.println(personagem.nome + " entrou na batalha");
            }
        }
        this.estadoBatalha = "Em andamento";
        System.out.println("Batalha iniciada");
    }

    public Personagem buscarPersonagemPorId(int id) {
        for (Personagem p : referencia) {
            if (p != null && p.getIdPersonagem() == id) {
                return p;
            }
        }
        return null;
    }

    public void executarTurno() {
        if (!"Em andamento".equals(estadoBatalha)) {
            System.out.println("A batalha não está em andamento");
            return;
        }
    
        if (fugaOcorreu) {
            System.out.println("A batalha foi encerrada devido à um fujão");
            return;
        }
    
        if (listaDeParticipantes.head == null) {
            System.out.println("Não há participantes na batalha");
            estadoBatalha = "Finalizada";
            verificarVencedor();
            return;
        }

        int idPersonagemAtual = listaDeParticipantes.head.data;
        listaDeParticipantes.remove();

        Personagem personagem = buscarPersonagemPorId(idPersonagemAtual);
        if (personagem == null) {
            System.out.println("Personagem com ID " + idPersonagemAtual + " não encontrado");
            return;
        }

        if (!personagem.estaVivo()) {
            System.out.println(personagem.nome + " está morto e não pode agir");
            rankingFinal.push(personagem.getIdPersonagem());
            return;
        }

        System.out.println("\n*** Turno " + turnoAtual + " ***");
        System.out.println("Vez de: " + personagem.nome);
        System.out.printf("Status: Vida %d/%d | Mana %d/%d\n",
                         personagem.vidaAtual, personagem.vidaMaxima,
                         personagem.manaAtual, personagem.manaMaxima);

        boolean isInimigo = true;
        for (Jogador j : App.jogadores) {
            if (j.personagens.buscar(personagem.nome) != null) {
                isInimigo = false;
                break;
            }
        }

        Personagem alvo = isInimigo ? 
            encontrarAlvoParaInimigo(personagem) : 
            encontrarAlvoVivo(personagem);

        if (alvo == null) {
            System.out.println("Nenhum alvo disponível, batalha encerrada");
            rankingFinal.push(personagem.getIdPersonagem());
            estadoBatalha = "Finalizada";
            verificarVencedor();
            return;
        }

        if (isInimigo) {
            executarTurnoInimigo(personagem, alvo);
        } else {
            executarAcaoPersonagem(personagem, alvo);
        }

        if (!personagem.estaVivo()) {
            System.out.println(personagem.nome + " foi derrotado");
            rankingFinal.push(personagem.getIdPersonagem());
        }

        if (!alvo.estaVivo()) {
            System.out.println(alvo.nome + " foi derrotado");
            rankingFinal.push(alvo.getIdPersonagem());
        }

        if (personagem.estaVivo()) {
            listaDeParticipantes.add(personagem.getIdPersonagem());
        }

        turnoAtual++;
        verificarVencedor();
    }

    private void executarTurnoInimigo(Personagem inimigo, Personagem alvo) {
        System.out.println("\n*** " + inimigo.nome + " vai agir ***");
        System.out.println("Habilidades disponíveis:");
        NodeHabilidade atual = inimigo.habilidades.inicio;
        if (atual == null) {
            System.out.println("- Nenhuma habilidade disponível.");
        }
        while (atual != null) {
            Habilidade h = atual.habilidade;
            System.out.printf("- %s (Dano: %d, Custo: %d)\n", h.nome, h.dano, h.custoMana);
            atual = atual.proximo;
        }

        int acao = (int)(Math.random() * 3);

        if (acao < 2 || inimigo.habilidades.inicio == null) {
            int dano = 3 + (inimigo.nivel / 2);
            System.out.println(inimigo.nome + " ataca " + alvo.nome + "!");
            alvo.receberDano(dano);
        } else {
            atual = inimigo.habilidades.inicio;
            List<Habilidade> habilidadesDisponiveis = new ArrayList<>();

            while (atual != null) {
                if (inimigo.manaAtual >= atual.habilidade.custoMana) {
                    habilidadesDisponiveis.add(atual.habilidade);
                }
                atual = atual.proximo;
            }

            if (!habilidadesDisponiveis.isEmpty()) {
                Habilidade habilidade = habilidadesDisponiveis.get(
                    (int)(Math.random() * habilidadesDisponiveis.size()));
                System.out.println(inimigo.nome + " usa " + habilidade.nome + " em " + alvo.nome + "!");
                inimigo.usarHabilidade(habilidade.id, alvo);
            } else {
                int dano = 3 + (inimigo.nivel / 2);
                System.out.println(inimigo.nome + " ataca " + alvo.nome + "!");
                alvo.receberDano(dano);
            }
        }
    }

    private Personagem encontrarAlvoParaInimigo(Personagem inimigo) {
        for (Personagem p : referencia) {
            if (p != null && p.estaVivo() && p.getIdPersonagem() != inimigo.getIdPersonagem()) {
                for (Jogador j : App.jogadores) {
                    if (j.personagens.buscar(p.nome) != null) {
                        return p;
                    }
                }
            }
        }
        
        for (Personagem p : referencia) {
            if (p != null && p.estaVivo() && p.getIdPersonagem() != inimigo.getIdPersonagem()) {
                boolean isInimigo = true;
                for (Jogador j : App.jogadores) {
                    if (j.personagens.buscar(p.nome) != null) {
                        isInimigo = false;
                        break;
                    }
                }
                if (!isInimigo) {
                    return p;
                }
            }
        }
        
        return null;
    }

    private void executarAcaoPersonagem(Personagem atacante, Personagem alvo) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\nOpções:");
        System.out.println("1. Atacar");
        if (atacante.habilidades.inicio != null) {
            System.out.println("2. Usar habilidade");
        }
        System.out.println("3. Fugir");
        System.out.print("Escolha uma opção: ");
        
        try {
            int escolha = Integer.parseInt(scanner.nextLine());
            
            switch (escolha) {
                case 1:
                    int danoBase = 5 + (atacante.nivel / 2);
                    System.out.println(atacante.nome + " usou um ataque básico");
                    alvo.receberDano(danoBase);
                    break;
                    
                case 2:
                    if (atacante.habilidades.inicio != null) {
                        usarHabilidade(atacante, alvo);
                    } else {
                        System.out.println("Opção inválida");
                        executarAcaoPersonagem(atacante, alvo); 
                    }
                    break;
                    
                case 3:
                    fugirBatalha(atacante);
                    break;
                    
                default:
                    System.out.println("Opção inválida");
                    executarAcaoPersonagem(atacante, alvo); 
            }
        } catch (NumberFormatException e) {
            System.out.println("Digite um número válido");
            executarAcaoPersonagem(atacante, alvo);
        }
    }

    private void usarHabilidade(Personagem atacante, Personagem alvo) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nHabilidades disponíveis:");
        NodeHabilidade atual = atacante.habilidades.inicio;
        int i = 1;
        
        while (atual != null) {
            Habilidade h = atual.habilidade;
            System.out.printf("%d. %s (Dano: %d, Custo: %d)\n",
                              i++, h.nome, h.dano, h.custoMana);
            atual = atual.proximo;
        }
        
        System.out.print("Escolha uma habilidade (0 para cancelar): ");
        
        try {
            int escolha = Integer.parseInt(scanner.nextLine());
            
            if (escolha > 0) {
                atual = atacante.habilidades.inicio;
                for (int j = 1; j < escolha && atual != null; j++) {
                    atual = atual.proximo;
                }
                
                if (atual != null) {
                    Habilidade habilidade = atual.habilidade;
                    if (atacante.manaAtual >= habilidade.custoMana) {
                        atacante.usarHabilidade(habilidade.id, alvo);
                    } else {
                        System.out.println("Mana insuficiente para esta habilidade");
                        usarHabilidade(atacante, alvo);
                    }
                } else {
                    System.out.println("Habilidade inválida");
                    usarHabilidade(atacante, alvo);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Digite um número válido");
            usarHabilidade(atacante, alvo);
        }
    }

    private void fugirBatalha(Personagem fugitivo) {
        System.out.println(fugitivo.nome + " fugiu da batalha");
        
        this.fugaOcorreu = true;
        this.fugitivo = fugitivo;
    
        listaDeParticipantes.removerPorId(fugitivo.getIdPersonagem()); 
    
        estadoBatalha = "Finalizada";
    }

    private Personagem encontrarAlvoVivo(Personagem atacante) {
        if (referencia == null) return null;
        
        for (Personagem p : referencia) {
            if (p != null && p.estaVivo() && p.getIdPersonagem() != atacante.getIdPersonagem()) {
                return p;
            }
        }
        return null;
    }

    public void verificarVencedor() {
        if (!"Finalizada".equals(estadoBatalha)) {
            if (fugaOcorreu) {
                estadoBatalha = "Finalizada";
                return;
            }
    
            int participantesVivos = 0;
            Personagem vencedor = null;
            
            for (Personagem p : referencia) {
                if (p != null && p.estaVivo()) {
                    participantesVivos++;
                    vencedor = p;
                }
            }
            
            if (participantesVivos <= 1) {
                estadoBatalha = "Finalizada";
                if (vencedor != null) {
                    rankingFinal.push(vencedor.getIdPersonagem());
                    System.out.println("O vencedor é: " + vencedor.nome);
                } else {
                    System.out.println("Todos os participantes foram derrotados, ninguém venceu");
                }
            }
        }
    }

    public void exibirRankingNormal() {
        System.out.println("\n*** Ranking Final ***");

        int totalParticipantes = 0;
        Node atual = rankingFinal.head;
        while (atual != null) {
            totalParticipantes++;
            atual = atual.next;
        }
        int posicao = 1;
        atual = rankingFinal.head;
        while (atual != null) {
            Personagem p = buscarPersonagemPorId(atual.data);
            if (p != null) {
                if (p.estaVivo()) {
                    System.out.println(posicao + ". " + p.nome + " (Vencedor)");
                } else {
                    System.out.println(posicao + ". " + p.nome);
                }
                posicao++;
            }
            atual = atual.next;
        }
    }

    public void exibirRankingFuga() {
        System.out.println("\n*** Ranking Final ***");
        
        int posicao = 1;
        for (Personagem p : referencia) {
            if (p != null && p.estaVivo() && !p.equals(fugitivo)) {
                System.out.println(posicao++ + ". " + p.nome);
            }
        }
       
        if (fugitivo != null) {
            System.out.println(posicao + ". " + fugitivo.nome + " (Fugiu)");
        }
    }   

    public int getIdBatalha() {
        return idBatalha;
    }

    public int getTurnoAtual() {
        return turnoAtual;
    }

    public String getEstadoBatalha() {
        return estadoBatalha;
    }
}