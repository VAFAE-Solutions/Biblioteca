import static org.example.demo.service.AutenticacaoService.gerarHash;

public class main {
    public static void main(String[] args) {
        String senha = "@Alfa@";
        String hash = gerarHash(senha);
        System.out.println("Hash gerado: " + hash);
    }
}