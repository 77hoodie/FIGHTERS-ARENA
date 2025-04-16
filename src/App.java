import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static List<Jogador> jogadores = new ArrayList<>();
    private static Jogador jogadorAtual = null;
    private static Personagem personagemAtual = null;
    private static Scanner scanner = new Scanner(System.in);
    private static Arena arenaAtual = null;

    public static void main(String[] args) {
        devProfiles();
        exibirMenuInicial();
    }

    private static void devProfiles() {
        Jogador jogador1 = new Jogador("admin", "123");
        Jogador jogador2 = new Jogador("notadmin", "456");
        
        jogador1.criarPersonagem("Sigrid, o Trapaceiro");
        jogador1.criarPersonagem("Frieren, a Maga");
        jogador2.criarPersonagem("Ymur, o Arqueiro Real");
        
        Personagem guerreiro = jogador1.selecionarPersonagem("Sigrid, o Trapaceiro");
        guerreiro.habilidades.adicionar(new Habilidade(1, "Facada", 1, 75));
        guerreiro.habilidades.adicionar(new Habilidade(2, "Chute fraco", 5, 5));
        
        Personagem mago = jogador1.selecionarPersonagem("Frieren, a Maga");
        mago.habilidades.adicionar(new Habilidade(1, "Zoltraak", 1, 90));
        mago.habilidades.adicionar(new Habilidade(2, "Judrajim", 1, 70));
        
        Personagem arqueiro = jogador2.selecionarPersonagem("Ymur, o Arqueiro Real");
        arqueiro.habilidades.adicionar(new Habilidade(1, "Flecha precisa", 5, 12));
        
        jogadores.add(jogador1);
        jogadores.add(jogador2);
    }

    private static void exibirMenuInicial() {
        while (true) {
            System.out.println("\n*** FIGHTER'S ARENA ***");
            System.out.println("1. Login");
            System.out.println("2. Cadastrar novo jogador");
            System.out.println("3. Sair");
            System.out.print("Escolha: ");
            
            try {
                int opcao = Integer.parseInt(scanner.nextLine());
                
                switch (opcao) {
                    case 1:
                        login();
                        break;
                    case 2:
                        cadastrarJogador();
                        break;
                    case 3:
                        System.out.println("Saindo do jogo...");
                        System.exit(0);
                    default:
                        System.out.println("Opção inválida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido");
            }
        }
    }

    private static void login() {
        System.out.print("\nUsuário: ");
        String usuario = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        
        for (Jogador jogador : jogadores) {
            if (jogador.getNome().equals(usuario) && jogador.autenticar(senha)) {
                jogadorAtual = jogador;
                System.out.println("\nBem-vindo, " + usuario);
                exibirMenuPrincipal();
                return;
            }
        }
        System.out.println("Usuário ou senha incorretos");
    }

    private static void cadastrarJogador() {
        System.out.print("\nNovo usuário: ");
        String usuario = scanner.nextLine();
        System.out.print("Nova senha: ");
        String senha = scanner.nextLine();
        
        if (usuario.isEmpty() || senha.isEmpty()) {
            System.out.println("Usuário e senha não podem estar vazios");
            return;
        }
        
        for (Jogador jogador : jogadores) {
            if (jogador.getNome().equals(usuario)) {
                System.out.println("Este nome já está sendo usado");
                return;
            }
        }
        
        Jogador novoJogador = new Jogador(usuario, senha);
        jogadores.add(novoJogador);
        System.out.println("Cadastro realizado com sucesso");
    }

    private static void exibirMenuPrincipal() {
        while (jogadorAtual != null) {
            System.out.println("\n*** MENU PRINCIPAL ***");
            System.out.println("Jogador: " + jogadorAtual.getNome());
            System.out.println("1. Gerenciar personagens");
            System.out.println("2. Iniciar batalha");
            System.out.println("3. Loja");
            System.out.println("4. Logout");
            System.out.print("Escolha: ");
            
            try {
                int opcao = Integer.parseInt(scanner.nextLine());
                
                switch (opcao) {
                    case 1:
                        exibirMenuPersonagens();
                        break;
                    case 2:
                        exibirMenuBatalha();
                        break;
                        case 3:
                        loja();
                        break;
                    case 4:
                        jogadorAtual = null;
                        personagemAtual = null;
                        return;                    
                    default:
                        System.out.println("Opção inválida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido");
            }
        }
    }

    private static void exibirMenuPersonagens() {
        while (true) {
            System.out.println("\n*** GERENCIAR PERSONAGENS ***");
            System.out.println("1. Criar novo personagem");
            System.out.println("2. Listar personagens");
            System.out.println("3. Selecionar personagem");
            System.out.println("4. Voltar");
            System.out.print("Escolha: ");
            
            try {
                int opcao = Integer.parseInt(scanner.nextLine());
                
                switch (opcao) {
                    case 1:
                        criarPersonagem();
                        break;
                    case 2:
                        listarPersonagens();
                        break;
                    case 3:
                        selecionarPersonagem();
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Opção inválida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido");
            }
        }
    }

    private static void criarPersonagem() {
        System.out.print("\nNome do personagem: ");
        String nome = scanner.nextLine();
        
        if (nome == null || nome.trim().isEmpty()) {
            System.out.println("Nome inválido");
            return;
        }
        
        jogadorAtual.criarPersonagem(nome);
        System.out.println("Personagem criado com sucesso");
    }

    private static void listarPersonagens() {
        System.out.println("\n*** SEUS PERSONAGENS ***");
        NodePersonagem atual = jogadorAtual.personagens.inicio;
        int i = 1;
        
        if (atual == null) {
            System.out.println("Você não tem personagens ainda, crie um");
            return;
        }
        
        while (atual != null) {
            Personagem p = atual.personagem;
            System.out.println(i++ + ". " + p.nome + " (Nível " + p.nivel + ")");
            System.out.println("   Vida: " + p.vidaAtual + "/" + p.vidaMaxima);
            System.out.println("   Mana: " + p.manaAtual + "/" + p.manaMaxima);
            System.out.println("   Habilidades: " + contarHabilidades(p));
            atual = atual.proximo;
        }
    }

    private static int contarHabilidades(Personagem p) {
        int count = 0;
        NodeHabilidade atual = p.habilidades.inicio;
        while (atual != null) {
            count++;
            atual = atual.proximo;
        }
        return count;
    }

    private static void selecionarPersonagem() {
        listarPersonagens();
        System.out.print("\nEscolha um personagem (0 para cancelar): ");
        
        try {
            int escolha = Integer.parseInt(scanner.nextLine());
            
            if (escolha == 0) return;
            
            NodePersonagem atual = jogadorAtual.personagens.inicio;
            for (int i = 1; i < escolha && atual != null; i++) {
                atual = atual.proximo;
            }
            
            if (atual != null) {
                personagemAtual = atual.personagem;
                System.out.println("Personagem selecionado: " + personagemAtual.nome);
            } else {
                System.out.println("Seleção inválida");
            }
        } catch (NumberFormatException e) {
            System.out.println("Digite um número válido");
        }
    }

    private static void exibirMenuBatalha() {
        if (personagemAtual == null) {
            System.out.println("Selecione um personagem primeiro");
            return;
        }
        
        while (true) {
            System.out.println("\n*** MENU DE BATALHA ***");
            System.out.println("1. PvE (Jogador vs Inimigos aleatórios)");
            System.out.println("2. PvP (Batalha local)");
            System.out.println("3. Voltar");
            System.out.print("Escolha: ");
            
            try {
                int opcao = Integer.parseInt(scanner.nextLine());
                
                switch (opcao) {
                    case 1:
                        iniciarBatalhaPvE();
                        break;
                    case 2:
                        iniciarBatalhaPvP();
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Opção inválida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido");
            }
        }
    }

    private static void iniciarBatalhaPvE() {
        System.out.print("\nQuantos inimigos deseja enfrentar? (1-4): ");
        
        try {
            int numInimigos = Integer.parseInt(scanner.nextLine());
            
            if (numInimigos < 1 || numInimigos > 4) {
                System.out.println("Número inválido, usando 1 inimigo");
                numInimigos = 1;
            }
        
            Personagem[] participantes = new Personagem[numInimigos + 1]; 
            participantes[0] = personagemAtual;
            
            for (int i = 1; i <= numInimigos; i++) {
                participantes[i] = criarInimigo(personagemAtual.nivel);
            }
            
            arenaAtual = new Arena(1, participantes.length);
            arenaAtual.iniciarBatalha(participantes);
            
            executarBatalha();
        } catch (NumberFormatException e) {
            System.out.println("Digite um número válido");
        }
    }
    
    private static Personagem criarInimigo(int nivelJogador) {
        String[] nomesInimigos = {"Duend, o Goblin", "Niesvald, o Bárbaro", "Skel, o Esqueleto", "Willheim, o Bruxo", "Hearald, o Lobisomem"};
        String nomeInimigo = nomesInimigos[(int)(Math.random() * nomesInimigos.length)] + " (Nível " + nivelJogador + ")";
        
        Personagem inimigo = new Personagem(nomeInimigo);
        inimigo.nivel = nivelJogador;
        inimigo.vidaMaxima = 70 + (nivelJogador * 5);
        inimigo.vidaAtual = inimigo.vidaMaxima;
        inimigo.manaMaxima = 20 + (nivelJogador * 3);
        inimigo.manaAtual = inimigo.manaMaxima;
        
        int numHabilidades = 1 + (int)(Math.random() * 2); 
        for (int i = 0; i < numHabilidades; i++) {
            int danoBase = 5 + nivelJogador + (int)(Math.random() * 5);
            int custoMana = 5 + (int)(Math.random() * 10);
            String[] nomesHabilidades = {"Chute", "Investida", "Raio", "Veneno", "Soco"};
            String nomeHabilidade = nomesHabilidades[(int)(Math.random() * nomesHabilidades.length)];
            
            inimigo.habilidades.adicionar(new Habilidade(i+1, nomeHabilidade, custoMana, danoBase));
        }
        
        return inimigo;
    }

    private static void iniciarBatalhaPvP() {
        System.out.println("\n*** SELECIONE O OPONENTE ***");
        
        int i = 1;
        List<Jogador> outrosJogadores = new ArrayList<>();
        for (Jogador j : jogadores) {
            if (!j.equals(jogadorAtual)) {
                System.out.println(i++ + ". " + j.getNome());
                outrosJogadores.add(j);
            }
        }
        
        if (outrosJogadores.isEmpty()) {
            System.out.println("Não há outros jogadores locais");
            return;
        }

        System.out.print("Escolha um oponente (0 para cancelar): ");
        
        try {
            int escolha = Integer.parseInt(scanner.nextLine());
            
            if (escolha == 0 || escolha > outrosJogadores.size()) {
                System.out.println("Seleção inválida");
                return;
            }
            
            Jogador oponente = outrosJogadores.get(escolha - 1);
            Personagem personagemOponente = selecionarPersonagemOponente(oponente);
            
            if (personagemOponente == null) {
                System.out.println("O oponente não tem personagens válidos");
                return;
            }
            
            Personagem[] participantes = new Personagem[2];
            participantes[0] = personagemAtual;
            participantes[1] = personagemOponente;
            
            arenaAtual = new Arena(2, 10);
            arenaAtual.iniciarBatalha(participantes);
            
            executarBatalha();
        } catch (NumberFormatException e) {
            System.out.println("Digite um número válido");
        }
    }

    private static Personagem selecionarPersonagemOponente(Jogador oponente) {
        System.out.println("\n*** SELECIONE O PERSONAGEM DO JOGADOR 2 ***");
        
        NodePersonagem atual = oponente.personagens.inicio;
        int i = 1;
        List<Personagem> personagens = new ArrayList<>();
        
        while (atual != null) {
            Personagem p = atual.personagem;
            System.out.println(i++ + ". " + p.nome + " (Nível " + p.nivel + ")");
            personagens.add(p);
            atual = atual.proximo;
        }
        
        System.out.print("Escolha um personagem (0 para cancelar): ");
        
        try {
            int escolha = Integer.parseInt(scanner.nextLine());
            
            if (escolha == 0 || escolha > personagens.size()) {
                return null;
            }
            
            return personagens.get(escolha - 1);
        } catch (NumberFormatException e) {
            System.out.println("Digite um número válido");
            return null;
        }
    }

    private static void executarBatalha() {
        System.out.println("\n*** BATALHA INICIADA ***");
        
        while ("Em andamento".equals(arenaAtual.getEstadoBatalha())) {
            System.out.println("\nPressione Enter para avançar o turno...");
            scanner.nextLine();
            
            arenaAtual.executarTurno();
            
            System.out.println("\n*** STATUS DA BATALHA ***");
            for (Personagem p : arenaAtual.referencia) {
                if (p != null) {
                    String status = p.estaVivo() ? "Vivo" : "Derrotado";
                    System.out.printf("%s: Vida %d/%d | Mana %d/%d | %s\n",
                                    p.nome, p.vidaAtual, p.vidaMaxima,
                                    p.manaAtual, p.manaMaxima, status);
                }
            }
        }   
        processarResultadoBatalha();
    }

    private static void processarResultadoBatalha() {
        
        if(arenaAtual.fugaOcorreu) {
            arenaAtual.exibirRankingFuga();
        }else{
            arenaAtual.exibirRankingNormal();
        }

        if (arenaAtual.fugaOcorreu && arenaAtual.fugitivo != null && arenaAtual.fugitivo.equals(personagemAtual)) {
            System.out.println("\nVocê fugiu da batalha, covarde...");
            personagemAtual.vidaAtual = personagemAtual.vidaMaxima;
            return;
        }
    
        boolean jogadorVenceu = false;
        for (Personagem p : arenaAtual.referencia) {
            if (p != null && p.estaVivo() && p.equals(personagemAtual)) {
                jogadorVenceu = true;
                break;
            }
        }
    
        if (jogadorVenceu) {
            System.out.println("\nVocê venceu a batalha!");
            personagemAtual.subirNivel();
            jogadorAtual.saldoMoedas += 50 * arenaAtual.referencia.length;
            System.out.println(personagemAtual.nome + " subiu para o nível " + 
                              personagemAtual.nivel + " e ganhou " + 
                              (50 * arenaAtual.referencia.length) + " moedas");
            
            if (personagemAtual.nivel <= 5) {
                aprenderNovaHabilidade(personagemAtual);
            }
        } else {
            System.out.println("\nVocê foi morto");
            personagemAtual.vidaAtual = personagemAtual.vidaMaxima / 2;
            System.out.println(personagemAtual.nome + " foi revivido com 50% de vida");
        }
    
        System.out.println("\nPressione Enter para continuar...");
        scanner.nextLine();
    }
    
    private static void aprenderNovaHabilidade(Personagem personagem) {
        String[] nomesHabilidades = {"Golpe baixo", "Insanidade", "Ataque rápidos consecutivos", 
                                    "Espadada cortante", "Investida", "Flecha perfurante", 
                                    "Raio de luz mágica", "Quebrar o chão", "Soco irritado"};
        
        String nomeHabilidade = nomesHabilidades[(int)(Math.random() * nomesHabilidades.length)];
        int danoBase = 5 + personagem.nivel * 2;
        int custoMana = 5 + personagem.nivel;
        
        Habilidade novaHabilidade = new Habilidade(
            contarHabilidades(personagem) + 1,
            nomeHabilidade,
            custoMana,
            danoBase
        );
        
        personagem.habilidades.adicionar(novaHabilidade);
        System.out.println(personagem.nome + " aprendeu a habilidade: " + nomeHabilidade);
    }

    private static void loja() {
        if (personagemAtual == null) {
            System.out.println("Selecione um personagem primeiro");
            return;
        }
    
        System.out.println("\n*** LOJA DE HABILIDADES ***");
        System.out.println("Saldo disponível: " + jogadorAtual.getSaldoMoedas() + " moedas");
        System.out.println("Habilidades disponíveis para compra:");
    
        String[] habilidadesLoja = {
            "Soco furioso (50 moedas)",
            "Técnica refinada (75 moedas)",
            "Catastravia (100 moedas)",
            "Vollzanbel (125 moedas)",
            "Zoltraak (150 moedas)"
        };
    
        int[] precos = {50, 75, 100, 125, 150};
        int[] danos = {15, 20, 40, 65, 80};
        int[] custosMana = {10, 15, 20, 25, 25};
    
        for (int i = 0; i < habilidadesLoja.length; i++) {
            System.out.println((i+1) + ". " + habilidadesLoja[i]);
        }
    
        System.out.println("0. Voltar");
        System.out.print("Escolha uma habilidade para comprar: ");
    
        try {
            int escolha = Integer.parseInt(scanner.nextLine());
    
            if (escolha == 0) return;
    
            if (escolha < 1 || escolha > habilidadesLoja.length) {
                System.out.println("Opção inválida");
                return;
            }
    
            int indice = escolha - 1;
            if (jogadorAtual.getSaldoMoedas() >= precos[indice]) {
                Habilidade novaHabilidade = new Habilidade(
                    contarHabilidades(personagemAtual) + 1,
                    habilidadesLoja[indice].split(" ")[0],
                    custosMana[indice],
                    danos[indice]
                );
    
                personagemAtual.habilidades.adicionar(novaHabilidade);
                jogadorAtual.saldoMoedas -= precos[indice];
                System.out.println("Habilidade " + novaHabilidade.nome + " comprada com sucesso");
            } else {
                System.out.println("Saldo insuficiente para comprar esta habilidade");
            }
        } catch (NumberFormatException e) {
            System.out.println("Digite um número válido");
        }
    }
}