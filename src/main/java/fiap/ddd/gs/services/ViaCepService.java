package fiap.ddd.gs.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;

public class ViaCepService {
    private static final String URL_TEMPLATE = "https://viacep.com.br/ws/%s/json/";

    public Endereco buscarEnderecoPorCep(String cep) {
        try {
            URL url = new URL(String.format(URL_TEMPLATE, cep));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            connection.disconnect();

            return new Gson().fromJson(content.toString(), Endereco.class);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar endereço pelo CEP: " + e.getMessage(), e);
        }
    }

    public static class Endereco {
        private String cep;
        private String logradouro;
        private String complemento;
        private String bairro;
        private String localidade;
        private String uf;

        // Getters e setters
        public String getCep() { return cep; }
        public void setCep(String cep) { this.cep = cep; }

        public String getLogradouro() { return logradouro; }
        public void setLogradouro(String logradouro) { this.logradouro = logradouro; }

        public String getComplemento() { return complemento; }
        public void setComplemento(String complemento) { this.complemento = complemento; }

        public String getBairro() { return bairro; }
        public void setBairro(String bairro) { this.bairro = bairro; }

        public String getLocalidade() { return localidade; }
        public void setLocalidade(String localidade) { this.localidade = localidade; }

        public String getUf() { return uf; }
        public void setUf(String uf) { this.uf = uf; }
    }
}
