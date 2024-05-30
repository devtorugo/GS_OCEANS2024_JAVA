package fiap.ddd.gs.entities;

public class Especimes extends _BaseEntity{

    private String nomeEspecie;
    private String localizacaoGeografica;
    private String descricao;
    private String ameacas;
    private Login login;


    public Especimes(){}

    public Especimes(int id, String nomeEspecie, String localizacaoGeografica, String descricao, String ameacas, Login login) {
        super(id);
        this.nomeEspecie = nomeEspecie;
        this.localizacaoGeografica = localizacaoGeografica;
        this.descricao = descricao;
        this.ameacas = ameacas;
        this.login = login;
    }

    public String getNomeEspecie() {
        return nomeEspecie;
    }

    public void setNomeEspecie(String nomeEspecie) {
        this.nomeEspecie = nomeEspecie;
    }

    public String getLocalizacaoGeografica() {
        return localizacaoGeografica;
    }

    public void setLocalizacaoGeografica(String localizacaoGeografica) {
        this.localizacaoGeografica = localizacaoGeografica;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getAmeacas() {
        return ameacas;
    }

    public void setAmeacas(String ameacas) {
        this.ameacas = ameacas;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }



    @Override
    public String toString() {
        return "Especimes{" +
                "nomeEspecie='" + nomeEspecie + '\'' +
                ", localizacaoGeografica='" + localizacaoGeografica + '\'' +
                ", descricao='" + descricao + '\'' +
                ", ameacas='" + ameacas + '\'' +
                ", login=" + login +
                "} " + super.toString();
    }
}
