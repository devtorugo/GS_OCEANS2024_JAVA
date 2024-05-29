package fiap.ddd.gs.entities;

public class Contato extends _BaseEntity{
    private String nome;
    private String telefone;
    private String mensagem;
    private Login login;

    public Contato(){}

    public Contato(int id, String nome, String telefone, String mensagem, Login login) {
        super(id);
        this.nome = nome;
        this.telefone = telefone;
        this.mensagem = mensagem;
        this.login = login;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "Contato{" +
                "nome='" + nome + '\'' +
                ", telefone='" + telefone + '\'' +
                ", mensagem='" + mensagem + '\'' +
                ", login=" + login +
                "} " + super.toString();
    }
}
