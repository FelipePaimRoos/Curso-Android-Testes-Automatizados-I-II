package leilao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import leilao.exception.LanceMenorQueUltimoLanceException;
import leilao.exception.LanceSeguidoDoMesmoUsuarioException;
import leilao.exception.UsuarioJaDeuCincoLancesException;

public class Leilao implements Serializable {

    private final String descricao;
    private final List<Lance> lances;
    private double maiorLance = 0.0;

    private double menorLance = 0.0;

    public Leilao(String descricao) {
        this.descricao = descricao;
        this.lances = new ArrayList<>();
    }

    public void propoe(Lance lance){
        validaLance(lance);
        lances.add(lance);
        double valorLance = lance.getValor();
        if (comecaAListaDeLances(valorLance)) return;
        Collections.sort(lances);
        calculaMaiorLance(valorLance);

    }

    private boolean comecaAListaDeLances(double valorLance) {
        if(lances.size() == 1){
            maiorLance = valorLance;
            menorLance = valorLance;
            return true;
        }
        return false;
    }

    private void validaLance(Lance lance) {
        double valorLance = lance.getValor();
        if (validaLanceExiste(valorLance)) throw new LanceMenorQueUltimoLanceException();
        if(!lances.isEmpty()){
            Usuario usuarioNovo = lance.getUsuario();
            if (validaUsuarioIgualDoUltimoLance(usuarioNovo)) throw new LanceSeguidoDoMesmoUsuarioException();
            if (validaUsuarioMaisDe5Lances(usuarioNovo)) throw new UsuarioJaDeuCincoLancesException();
        }
    }

    private boolean validaUsuarioMaisDe5Lances(Usuario usuarioNovo) {
        int lancesDoUsuario = 0;
        for (Lance l: lances) {
            Usuario usuarioExistente = l.getUsuario();
            if(usuarioExistente.equals(usuarioNovo)){
                lancesDoUsuario++;
                if(lancesDoUsuario == 5){
                    return true;
                }
            }

        }
        return false;
    }

    private boolean validaUsuarioIgualDoUltimoLance(Usuario usuarioNovo) {
        Usuario ultimoUsuario = lances.get(0).getUsuario();

        if(usuarioNovo.equals(ultimoUsuario)){
            return true;
        }
        return false;
    }

    private boolean validaLanceExiste(double valorLance) {
        if (maiorLance > valorLance){
            return true;
        }
        return false;
    }

    private void calculaMenorLance(double valorLance) {
        if (valorLance < menorLance){
            menorLance = valorLance;
        }
    }

    private void calculaMaiorLance(double valorLance) {
        if(valorLance > maiorLance){
            maiorLance = valorLance;
        }
    }

    public double getMaiorLance(){
        return maiorLance;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getMenorLance() {
        return menorLance;
    }

    public List<Lance> tresMaioresLances() {
        int quantidadeMaximaLances = lances.size();
        if (quantidadeMaximaLances > 3){
            quantidadeMaximaLances = 3;
        }
        return lances.subList(0, quantidadeMaximaLances);
    }

    public int quantidadeLances() {
        return lances.size();
    }
}
