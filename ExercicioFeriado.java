import java.util.ArrayList;
import java.util.List;

// ============================================================
// PADRÃO OBSERVER — Notificação de reservas
// ============================================================

// Interface Observer
interface ObservadorReserva {
    void notificar(String mensagem);
}

// Observer concreto: Notificação por E-mail
class NotificadorEmail implements ObservadorReserva {
    private String email;

    public NotificadorEmail(String email) {
        this.email = email;
    }

    @Override
    public void notificar(String mensagem) {
        System.out.println("  [E-MAIL para " + email + "] " + mensagem);
    }
}

// Observer concreto: Notificação por SMS
class NotificadorSMS implements ObservadorReserva {
    private String telefone;

    public NotificadorSMS(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public void notificar(String mensagem) {
        System.out.println("  [SMS para " + telefone + "] " + mensagem);
    }
}

// ============================================================
// PADRÃO STRATEGY — Estratégias de desconto
// ============================================================

// Interface Strategy
interface EstrategiaDesconto {
    double calcularDesconto(double valor);
    String getNome();
}

// Strategy concreto: Alta Temporada (5% de desconto)
class DescontoAltaTemporada implements EstrategiaDesconto {
    @Override
    public double calcularDesconto(double valor) {
        return valor * 0.05;
    }

    @Override
    public String getNome() {
        return "Alta Temporada (5%)";
    }
}

// Strategy concreto: Baixa Temporada (20% de desconto)
class DescontoBaixaTemporada implements EstrategiaDesconto {
    @Override
    public double calcularDesconto(double valor) {
        return valor * 0.20;
    }

    @Override
    public String getNome() {
        return "Baixa Temporada (20%)";
    }
}

// Strategy concreto: Promoção Relâmpago (35% de desconto)
class DescontoPromocional implements EstrategiaDesconto {
    @Override
    public double calcularDesconto(double valor) {
        return valor * 0.35;
    }

    @Override
    public String getNome() {
        return "Promoção Relâmpago (35%)";
    }
}

// ============================================================
// PADRÃO FACTORY + Pacotes base
// ============================================================

// Interface comum para todos os pacotes (também usada por Proxy e Decorator)
interface PacoteViagem {
    String getDescricao();
    double getPreco();
    boolean isPremium();
}

// Pacote concreto: Praia
class PacotePraia implements PacoteViagem {
    @Override
    public String getDescricao() {
        return "Pacote Praia (Resort Beira-Mar, 5 dias)";
    }

    @Override
    public double getPreco() {
        return 2500.00;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}

// Pacote concreto: Montanha
class PacoteMontanha implements PacoteViagem {
    @Override
    public String getDescricao() {
        return "Pacote Montanha (Chalé na Serra, 4 dias)";
    }

    @Override
    public double getPreco() {
        return 1800.00;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}

// Pacote concreto: Cidade
class PacoteCidade implements PacoteViagem {
    @Override
    public String getDescricao() {
        return "Pacote Cidade (Hotel Centro Histórico, 3 dias)";
    }

    @Override
    public double getPreco() {
        return 1200.00;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}

// Pacote concreto: Premium (pacote real usado pelo Proxy)
class PacotePremiumReal implements PacoteViagem {
    @Override
    public String getDescricao() {
        return "Pacote Premium (Suíte Presidencial + Cruzeiro, 7 dias)";
    }

    @Override
    public double getPreco() {
        return 8500.00;
    }

    @Override
    public boolean isPremium() {
        return true;
    }
}

// Fábrica de pacotes de viagem
class PacoteViagemFactory {
    public static PacoteViagem criarPacote(String tipo) {
        switch (tipo.toLowerCase()) {
            case "praia":
                return new PacotePraia();
            case "montanha":
                return new PacoteMontanha();
            case "cidade":
                return new PacoteCidade();
            case "premium":
                return new PacotePremiumReal();
            default:
                throw new IllegalArgumentException("Tipo de pacote desconhecido: " + tipo);
        }
    }
}

// ============================================================
// PADRÃO DECORATOR — Extras para os pacotes
// ============================================================

// Decorator abstrato
abstract class PacoteDecorator implements PacoteViagem {
    protected PacoteViagem pacoteDecorado;

    public PacoteDecorator(PacoteViagem pacoteDecorado) {
        this.pacoteDecorado = pacoteDecorado;
    }

    @Override
    public boolean isPremium() {
        return pacoteDecorado.isPremium();
    }
}

// Decorator concreto: Seguro Viagem (+R$150)
class SeguroViagemDecorator extends PacoteDecorator {
    public SeguroViagemDecorator(PacoteViagem pacote) {
        super(pacote);
    }

    @Override
    public String getDescricao() {
        return pacoteDecorado.getDescricao() + " + Seguro Viagem";
    }

    @Override
    public double getPreco() {
        return pacoteDecorado.getPreco() + 150.00;
    }
}

// Decorator concreto: Refeições Incluídas (+R$200)
class RefeicaoDecorator extends PacoteDecorator {
    public RefeicaoDecorator(PacoteViagem pacote) {
        super(pacote);
    }

    @Override
    public String getDescricao() {
        return pacoteDecorado.getDescricao() + " + Refeições Incluídas";
    }

    @Override
    public double getPreco() {
        return pacoteDecorado.getPreco() + 200.00;
    }
}

// Decorator concreto: Tour Guiado (+R$300)
class TourGuiadoDecorator extends PacoteDecorator {
    public TourGuiadoDecorator(PacoteViagem pacote) {
        super(pacote);
    }

    @Override
    public String getDescricao() {
        return pacoteDecorado.getDescricao() + " + Tour Guiado";
    }

    @Override
    public double getPreco() {
        return pacoteDecorado.getPreco() + 300.00;
    }
}

// ============================================================
// PADRÃO PROXY — Controle de acesso a pacotes premium
// ============================================================

class PacotePremiumProxy implements PacoteViagem {
    private PacoteViagem pacoteReal;
    private boolean usuarioAutenticado;

    public PacotePremiumProxy(boolean usuarioAutenticado) {
        this.usuarioAutenticado = usuarioAutenticado;
    }

    private void verificarAcesso() {
        if (!usuarioAutenticado) {
            throw new SecurityException("Acesso negado! Apenas usuários autenticados podem acessar pacotes premium.");
        }
    }

    private void carregarPacoteReal() {
        if (pacoteReal == null) {
            System.out.println("  [PROXY] Carregando pacote premium do sistema...");
            pacoteReal = PacoteViagemFactory.criarPacote("premium");
        }
    }

    @Override
    public String getDescricao() {
        verificarAcesso();
        carregarPacoteReal();
        return pacoteReal.getDescricao();
    }

    @Override
    public double getPreco() {
        verificarAcesso();
        carregarPacoteReal();
        return pacoteReal.getPreco();
    }

    @Override
    public boolean isPremium() {
        return true;
    }
}

// ============================================================
// PADRÃO ADAPTER — Adaptadores de sistemas de pagamento
// ============================================================

// Interface alvo (que o sistema espera)
interface ProcessadorPagamento {
    boolean processar(double valor);
    String getNomeSistema();
}

// Sistema legado de PIX (interface incompatível)
class SistemaPIX {
    public boolean efetuarTransferenciaPIX(String chave, double montante) {
        System.out.println("  [PIX] Transferência de R$" + String.format("%.2f", montante)
                + " efetuada via chave " + chave);
        return true;
    }
}

// Sistema legado de Cartão (interface incompatível)
class SistemaCartao {
    public int cobrarCartaoCredito(double total, int parcelas) {
        System.out.println("  [CARTÃO] Cobrança de R$" + String.format("%.2f", total)
                + " em " + parcelas + "x aprovada");
        return 0; // 0 = sucesso
    }
}

// Adaptador para o sistema PIX
class AdaptadorPIX implements ProcessadorPagamento {
    private SistemaPIX sistemaPIX;
    private String chavePIX;

    public AdaptadorPIX(SistemaPIX sistemaPIX, String chavePIX) {
        this.sistemaPIX = sistemaPIX;
        this.chavePIX = chavePIX;
    }

    @Override
    public boolean processar(double valor) {
        return sistemaPIX.efetuarTransferenciaPIX(chavePIX, valor);
    }

    @Override
    public String getNomeSistema() {
        return "PIX";
    }
}

// Adaptador para o sistema de Cartão de Crédito
class AdaptadorCartao implements ProcessadorPagamento {
    private SistemaCartao sistemaCartao;
    private int parcelas;

    public AdaptadorCartao(SistemaCartao sistemaCartao, int parcelas) {
        this.sistemaCartao = sistemaCartao;
        this.parcelas = parcelas;
    }

    @Override
    public boolean processar(double valor) {
        int resultado = sistemaCartao.cobrarCartaoCredito(valor, parcelas);
        return resultado == 0;
    }

    @Override
    public String getNomeSistema() {
        return "Cartão de Crédito (" + parcelas + "x)";
    }
}

// ============================================================
// PADRÃO SINGLETON — Gerenciador central de reservas
// ============================================================

class GerenciadorReservas {
    private static GerenciadorReservas instancia;
    private List<String> reservas;
    private List<ObservadorReserva> observadores;

    // Construtor privado — impede instanciação externa
    private GerenciadorReservas() {
        this.reservas = new ArrayList<>();
        this.observadores = new ArrayList<>();
    }

    // Método de acesso à instância única
    public static GerenciadorReservas getInstancia() {
        if (instancia == null) {
            instancia = new GerenciadorReservas();
        }
        return instancia;
    }

    // Gerenciamento de Observers
    public void adicionarObservador(ObservadorReserva observador) {
        observadores.add(observador);
    }

    public void removerObservador(ObservadorReserva observador) {
        observadores.remove(observador);
    }

    private void notificarObservadores(String mensagem) {
        for (ObservadorReserva obs : observadores) {
            obs.notificar(mensagem);
        }
    }

    // Registra uma nova reserva e notifica observadores
    public void registrarReserva(String cliente, PacoteViagem pacote, double valorFinal) {
        String registro = "Reserva de " + cliente + " — " + pacote.getDescricao()
                + " — Valor: R$" + String.format("%.2f", valorFinal);
        reservas.add(registro);

        System.out.println("  [SINGLETON] Reserva #" + reservas.size() + " registrada com sucesso!");
        notificarObservadores("Nova reserva confirmada para " + cliente
                + ": " + pacote.getDescricao() + " por R$" + String.format("%.2f", valorFinal));
    }

    public void listarReservas() {
        System.out.println("\n  === Todas as Reservas Registradas ===");
        for (int i = 0; i < reservas.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + reservas.get(i));
        }
    }
}

// ============================================================
// PADRÃO FACADE — Fachada para simplificar o fluxo de reserva
// ============================================================

class ReservaFacade {
    private GerenciadorReservas gerenciador;

    public ReservaFacade() {
        this.gerenciador = GerenciadorReservas.getInstancia();
    }

    /**
     * Método simplificado que orquestra todo o processo de reserva:
     * 1. Cria o pacote via Factory
     * 2. Adiciona extras via Decorators
     * 3. Calcula desconto via Strategy
     * 4. Processa pagamento via Adapter
     * 5. Registra reserva no Singleton
     * 6. Notifica Observers
     */
    public boolean realizarReservaCompleta(String cliente, String tipoPacote,
            boolean comSeguro, boolean comRefeicao, boolean comTour,
            EstrategiaDesconto estrategia, ProcessadorPagamento pagamento) {

        System.out.println("\n  --- Iniciando reserva para " + cliente + " ---");

        // 1. Factory: cria o pacote
        PacoteViagem pacote = PacoteViagemFactory.criarPacote(tipoPacote);
        System.out.println("  [FACTORY] Pacote criado: " + pacote.getDescricao());

        // 2. Decorator: adiciona extras
        if (comSeguro) {
            pacote = new SeguroViagemDecorator(pacote);
        }
        if (comRefeicao) {
            pacote = new RefeicaoDecorator(pacote);
        }
        if (comTour) {
            pacote = new TourGuiadoDecorator(pacote);
        }
        System.out.println("  [DECORATOR] Pacote final: " + pacote.getDescricao());
        System.out.println("  [DECORATOR] Preço bruto: R$" + String.format("%.2f", pacote.getPreco()));

        // 3. Strategy: calcula desconto
        double desconto = estrategia.calcularDesconto(pacote.getPreco());
        double valorFinal = pacote.getPreco() - desconto;
        System.out.println("  [STRATEGY] Estratégia: " + estrategia.getNome()
                + " → Desconto: R$" + String.format("%.2f", desconto));
        System.out.println("  [STRATEGY] Valor final: R$" + String.format("%.2f", valorFinal));

        // 4. Adapter: processa pagamento
        System.out.println("  [ADAPTER] Processando pagamento via " + pagamento.getNomeSistema() + "...");
        boolean pagamentoOk = pagamento.processar(valorFinal);

        if (pagamentoOk) {
            // 5 e 6. Singleton registra + Observer notifica
            gerenciador.registrarReserva(cliente, pacote, valorFinal);
            System.out.println("  --- Reserva concluída com sucesso! ---");
            return true;
        } else {
            System.out.println("  [ERRO] Falha no pagamento. Reserva cancelada.");
            return false;
        }
    }
}

// ============================================================
// CLASSE PRINCIPAL — Demonstração de todos os padrões
// ============================================================

public class ExercicioFeriado {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║         SISTEMA FERIADOFELIZ — RESERVA DE VIAGENS           ║");
        System.out.println("║    Demonstração de 8 Padrões de Projeto em Java             ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");

        // ============================================================
        // 1. OBSERVER — Registrar observadores no Singleton
        // ============================================================
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("  1. PADRÃO OBSERVER — Registrando notificadores");
        System.out.println("══════════════════════════════════════════");

        GerenciadorReservas gerenciador = GerenciadorReservas.getInstancia();
        ObservadorReserva emailObs = new NotificadorEmail("cliente@feriadofeliz.com.br");
        ObservadorReserva smsObs = new NotificadorSMS("(11) 99999-1234");

        gerenciador.adicionarObservador(emailObs);
        gerenciador.adicionarObservador(smsObs);
        System.out.println("  Observadores registrados: E-mail e SMS");

        // ============================================================
        // 2. FACTORY — Criar pacotes de viagem
        // ============================================================
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("  2. PADRÃO FACTORY — Criando pacotes");
        System.out.println("══════════════════════════════════════════");

        PacoteViagem pacotePraia = PacoteViagemFactory.criarPacote("praia");
        PacoteViagem pacoteMontanha = PacoteViagemFactory.criarPacote("montanha");
        PacoteViagem pacoteCidade = PacoteViagemFactory.criarPacote("cidade");

        System.out.println("  Pacote 1: " + pacotePraia.getDescricao()
                + " — R$" + String.format("%.2f", pacotePraia.getPreco()));
        System.out.println("  Pacote 2: " + pacoteMontanha.getDescricao()
                + " — R$" + String.format("%.2f", pacoteMontanha.getPreco()));
        System.out.println("  Pacote 3: " + pacoteCidade.getDescricao()
                + " — R$" + String.format("%.2f", pacoteCidade.getPreco()));

        // ============================================================
        // 3. DECORATOR — Adicionar extras ao pacote
        // ============================================================
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("  3. PADRÃO DECORATOR — Adicionando extras");
        System.out.println("══════════════════════════════════════════");

        PacoteViagem pacoteCompleto = new SeguroViagemDecorator(
                new RefeicaoDecorator(
                        new TourGuiadoDecorator(pacotePraia)));

        System.out.println("  Original:  " + pacotePraia.getDescricao()
                + " — R$" + String.format("%.2f", pacotePraia.getPreco()));
        System.out.println("  Decorado:  " + pacoteCompleto.getDescricao()
                + " — R$" + String.format("%.2f", pacoteCompleto.getPreco()));

        // ============================================================
        // 4. PROXY — Controle de acesso a pacotes premium
        // ============================================================
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("  4. PADRÃO PROXY — Acesso a pacote premium");
        System.out.println("══════════════════════════════════════════");

        // Tentativa SEM autenticação
        System.out.println("\n  → Tentando acessar sem autenticação...");
        PacoteViagem proxyNaoAutenticado = new PacotePremiumProxy(false);
        try {
            proxyNaoAutenticado.getDescricao();
        } catch (SecurityException e) {
            System.out.println("  ✗ " + e.getMessage());
        }

        // Tentativa COM autenticação
        System.out.println("\n  → Tentando acessar com autenticação...");
        PacoteViagem proxyAutenticado = new PacotePremiumProxy(true);
        System.out.println("  ✓ " + proxyAutenticado.getDescricao()
                + " — R$" + String.format("%.2f", proxyAutenticado.getPreco()));

        // ============================================================
        // 5. STRATEGY — Diferentes estratégias de desconto
        // ============================================================
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("  5. PADRÃO STRATEGY — Calculando descontos");
        System.out.println("══════════════════════════════════════════");

        double precoBase = pacoteCompleto.getPreco(); // R$3.150,00
        EstrategiaDesconto alta = new DescontoAltaTemporada();
        EstrategiaDesconto baixa = new DescontoBaixaTemporada();
        EstrategiaDesconto promo = new DescontoPromocional();

        System.out.println("  Preço base: R$" + String.format("%.2f", precoBase));
        System.out.println("  " + alta.getNome() + ": -R$"
                + String.format("%.2f", alta.calcularDesconto(precoBase))
                + " → R$" + String.format("%.2f", precoBase - alta.calcularDesconto(precoBase)));
        System.out.println("  " + baixa.getNome() + ": -R$"
                + String.format("%.2f", baixa.calcularDesconto(precoBase))
                + " → R$" + String.format("%.2f", precoBase - baixa.calcularDesconto(precoBase)));
        System.out.println("  " + promo.getNome() + ": -R$"
                + String.format("%.2f", promo.calcularDesconto(precoBase))
                + " → R$" + String.format("%.2f", precoBase - promo.calcularDesconto(precoBase)));

        // ============================================================
        // 6. ADAPTER — Sistemas de pagamento adaptados
        // ============================================================
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("  6. PADRÃO ADAPTER — Processando pagamentos");
        System.out.println("══════════════════════════════════════════");

        ProcessadorPagamento pagPIX = new AdaptadorPIX(new SistemaPIX(), "feriadofeliz@pix.com");
        ProcessadorPagamento pagCartao = new AdaptadorCartao(new SistemaCartao(), 3);

        System.out.println("  Pagamento via " + pagPIX.getNomeSistema() + ":");
        pagPIX.processar(2992.50);

        System.out.println("  Pagamento via " + pagCartao.getNomeSistema() + ":");
        pagCartao.processar(2992.50);

        // ============================================================
        // 7. SINGLETON — Verificando instância única
        // ============================================================
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("  7. PADRÃO SINGLETON — Gerenciador de Reservas");
        System.out.println("══════════════════════════════════════════");

        GerenciadorReservas gerenciador2 = GerenciadorReservas.getInstancia();
        System.out.println("  Mesma instância? " + (gerenciador == gerenciador2 ? "SIM ✓" : "NÃO ✗"));

        // ============================================================
        // 8. FACADE — Reserva completa simplificada
        // ============================================================
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("  8. PADRÃO FACADE — Reserva completa em uma chamada");
        System.out.println("══════════════════════════════════════════");

        ReservaFacade fachada = new ReservaFacade();

        // Reserva 1: Pacote Praia com seguro e refeição, desconto de baixa temporada, pago via PIX
        fachada.realizarReservaCompleta(
                "Ana Silva", "praia",
                true, true, false,
                new DescontoBaixaTemporada(),
                new AdaptadorPIX(new SistemaPIX(), "ana.silva@pix.com"));

        // Reserva 2: Pacote Montanha com tour guiado, promoção relâmpago, pago via cartão
        fachada.realizarReservaCompleta(
                "Carlos Souza", "montanha",
                false, false, true,
                new DescontoPromocional(),
                new AdaptadorCartao(new SistemaCartao(), 6));

        // Reserva 3: Pacote Cidade completo, alta temporada, pago via PIX
        fachada.realizarReservaCompleta(
                "Maria Oliveira", "cidade",
                true, true, true,
                new DescontoAltaTemporada(),
                new AdaptadorPIX(new SistemaPIX(), "maria@pix.com"));

        // ============================================================
        // Relatório final — lista todas as reservas (Singleton)
        // ============================================================
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("  RELATÓRIO FINAL (via Singleton)");
        System.out.println("══════════════════════════════════════════");
        gerenciador.listarReservas();

        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║  Padrões demonstrados: Factory, Singleton, Proxy, Adapter,  ║");
        System.out.println("║  Facade, Decorator, Strategy, Observer                      ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
    }
}
