import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

// Interface que todos os observadores devem implementar
interface Observer {
    void update(Subject s);
}

// Classe base do sujeito 
class Subject {
    private String local; 
    private List<Observer> observers = new ArrayList<Observer>();
    
    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }

    public void addObserver(Observer observer) { observers.add(observer); }
    public void removeObserver(Observer observer) { observers.remove(observer); }
    
    public void notifyObservers() {
        Iterator<Observer> it = observers.iterator();
        while (it.hasNext()) {
            Observer obs = it.next();
            obs.update(this); 
        }
    }
}

class Temperatura extends Subject  {
    private double temp;
    public Temperatura(String local) { setLocal(local); }
    public double getTemp() { return temp; }
    public void setTemp(double temp) {
        this.temp = temp;
        notifyObservers(); 
    }
}

class Ph extends Subject { 
    private double ph; 
    public Ph(String local) { setLocal(local); }
    public double getPh(){ return ph; }
    public void setPh(double ph){
        this.ph = ph;
        notifyObservers(); 
    }
}

class UmidadeAr extends Subject {
    private double umid_ar; 
    public UmidadeAr(String local) { setLocal(local); }
    public double getUmidadeAr(){ return umid_ar; }
    public void setUmidadeAr(double umid_ar){
        this.umid_ar = umid_ar;
        notifyObservers(); 
    }
}

// Estação de monitoramento. Quando uma estação é instanciada, ela já tem os 3 sensores.
class EstacaoMonitoramento {
    private String local;
    private Temperatura sensorTemp;
    private Ph sensorPh;
    private UmidadeAr sensorUmidade;

// O construtor precisa do nome do local e cria os 3 sensores 
    public EstacaoMonitoramento(String local) {
        this.local = local;
        this.sensorTemp = new Temperatura(local);
        this.sensorPh = new Ph(local);
        this.sensorUmidade = new UmidadeAr(local);
    }

// getters e setters
    public String getLocal() { return local; }
    public Temperatura getSensorTemp() { return sensorTemp; }
    public Ph getSensorPh() { return sensorPh; }
    public UmidadeAr getSensorUmidade() { return sensorUmidade; }

    public void atualizarTemperatura(double temp) { this.sensorTemp.setTemp(temp); }
    public void atualizarPh(double ph) { this.sensorPh.setPh(ph); }
    public void atualizarUmidadeAr(double umidade) { this.sensorUmidade.setUmidadeAr(umidade); }
}

class Universidade implements Observer  {
    private String nome;
    public Universidade(String nome) { this.nome = nome; }

    public void update(Subject s) { 
        if (s instanceof Temperatura) {
            Temperatura t = (Temperatura) s;
            System.out.println("Alteração na universidade " + this.nome + ". A Temperatura em " + t.getLocal() + " mudou para: " + t.getTemp() + "°C");
        } else if (s instanceof Ph) {
            Ph p = (Ph) s;
            System.out.println("Alteração na universidade " + this.nome + ". O pH em " + p.getLocal() + " mudou para: " + p.getPh());
        } else if (s instanceof UmidadeAr) {
            UmidadeAr u = (UmidadeAr) s;
            System.out.println("Alteração na universidade " + this.nome + ". A Umidade do ar em " + u.getLocal() + " mudou para: " + u.getUmidadeAr() + "%");
        }
    }
}

// Implementação do padrão Singleton
// Uma única central armazena todas as estações criadas no país
class CentralMonitoramento {
    private static CentralMonitoramento instancia;
    private List<EstacaoMonitoramento> todasAsEstacoes;

    private CentralMonitoramento() {
        todasAsEstacoes = new ArrayList<>();
    }

    public static CentralMonitoramento getInstance() {
        if (instancia == null) {
            instancia = new CentralMonitoramento();
        }
        return instancia;
    }

    public void registrarEstacao(EstacaoMonitoramento estacao) {
        todasAsEstacoes.add(estacao);
        System.out.println("[Central] Estação " + estacao.getLocal() + " registrada no sistema nacional.");
    }
}

// Implementação do padrão factory 
// Fábrica responsável por criar as estações e vinculá-las ao Singleton automaticamente
class EstacaoFactory {
    public static EstacaoMonitoramento criarEstacao(String local) {
        EstacaoMonitoramento novaEstacao = new EstacaoMonitoramento(local);
        // Registra na central por meio do Singleton
        CentralMonitoramento.getInstance().registrarEstacao(novaEstacao);
        return novaEstacao;
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Inicialização do sistema:");
        // Usando a Factory para criar as estações (ela já cadastra na Central)
        EstacaoMonitoramento manaus = EstacaoFactory.criarEstacao("Manaus");
        EstacaoMonitoramento fronteiraLeste = EstacaoFactory.criarEstacao("Fronteira Leste");

        Universidade sp = new Universidade("São Paulo");
        
        manaus.getSensorTemp().addObserver(sp);

        System.out.println("\nAtualizando dados de Manaus");
        manaus.atualizarTemperatura(32.5);
    }
}