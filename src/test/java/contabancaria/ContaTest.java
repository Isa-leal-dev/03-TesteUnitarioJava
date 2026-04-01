package contabancaria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Testes unitários para a classe Conta.
 *
 * PARTE 1 — Testes de exemplo (Construtor) já estão prontos.
 *           Observe o padrão AAA e o uso de @Test e @ParameterizedTest.
 *
 * PARTE 2 — Você deve escrever os testes para os demais métodos
 *           seguindo rigorosamente o ciclo TDD: Red → Green → Refactor.
 *
 * Para cada método da classe Conta, crie testes que cubram:
 *   ✅ O cenário de sucesso (caminho feliz)
 *   ❌ Cada regra de validação (cenários de exceção)
 *   🔄 Casos de borda (valores limites)
 */
class ContaTest {

    // =======================================================
    //  PARTE 1 — EXEMPLO GUIADO: Testes do Construtor
    //  Observe o padrão Arrange-Act-Assert (AAA)
    // =======================================================

    @Test
    void construtor_DadosValidos_CriaContaCorretamente() {
        // Arrange & Act
        var conta = new Conta("Maria", 100);

        // Assert
        assertEquals("Maria", conta.getTitular());
        assertEquals(100, conta.getSaldo());
        assertTrue(conta.isAtiva());
    }

    @Test
    void construtor_SemSaldoInicial_CriaContaComSaldoZero() {
        // Arrange & Act
        var conta = new Conta("João");

        // Assert
        assertEquals("João", conta.getTitular());
        assertEquals(0, conta.getSaldo());
        assertTrue(conta.isAtiva());
    }

    @Test
    void construtor_TitularNulo_LancaIllegalArgumentException() {
        // Assert — verifica que a exceção é lançada
        assertThrows(IllegalArgumentException.class, () -> new Conta(null));
    }

    @Test
    void construtor_TitularVazio_LancaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Conta(""));
    }

    @Test
    void construtor_SaldoNegativo_LancaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Conta("Maria", -50));
    }

    @ParameterizedTest
    @CsvSource({
        "Ana,    0",
        "Carlos, 1000",
        "Beatriz, 0.01"
    })
    void construtor_VariosValoresValidos_CriaContaCorretamente(String titular, double saldo) {
        // Act
        var conta = new Conta(titular, saldo);

        // Assert
        assertEquals(titular, conta.getTitular());
        assertEquals(saldo, conta.getSaldo(), 0.001);
        assertTrue(conta.isAtiva());
    }

    // =======================================================
    //  PARTE 2 — ESCREVA OS TESTES ABAIXO (TDD)
    //  Lembre-se: escreva o teste PRIMEIRO, veja FALHAR (Red),
    //  depois implemente o código para PASSAR (Green),
    //  e por fim faça Refactor se necessário.
    // =======================================================

    // =======================================================
    //  Testes para depositar
    // =======================================================

    @Test
    @DisplayName("01 - Depósito com valor válido atualiza o saldo")
    void depositar_ValorValido_AtualizaSaldo() {
        // Arrange
        var conta = new Conta("Maria", 100);

        // Act
        conta.depositar(50);
        
        // Assert
        assertEquals(150, conta.getSaldo());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, -0.01, -200})
    @DisplayName("02 - Depósito com valor zero/negativo lança IllegalArgumentException")
    void depositar_ValorZeroOuNegativo_LancaIllegalArgumentException(double valor){

        //Arrange:
        var conta = new Conta("Joana",1000);

        //Act e Assert:
        assertThrows(IllegalArgumentException.class, () -> conta.depositar(valor));
    }        

     @Test
     @DisplayName("03 - Depósito em conta inativa lança IllegalStateException")
     void depositar_ContaInativa_LancaIllegalStateException(){
        //Arrange:
        var conta = new Conta("Joana",0);
        conta.encerrar();

        //Act e Assert:
        assertThrows(IllegalStateException.class, () -> conta.depositar(100));       
     }

    // =======================================================
    //  Testes para sacar
    // =======================================================
   
    @Test
    @DisplayName("04 - Saque com valor válido atualiza o saldo")
    void sacar_ValorValido_AtualizaSaldo() {
        // Arrange
        var conta = new Conta("Nicolas", 1000);

        // Act
        conta.sacar(100);
        
        // Assert
        assertEquals(900, conta.getSaldo());
    }
  
    @Test
    @DisplayName("05 - Saque com valor maior que saldo lança IllegalStateException")
    void sacar_ValorMaiorSaldo_LancaIllegalStateException(){
        // Arrange
        var conta = new Conta("Nicolas", 1000);

        // Act e Assert
        assertThrows(IllegalStateException.class, () -> conta.sacar(1100));
    }
      
    @ParameterizedTest
    @ValueSource(doubles = {0, -0.01, -30})
    @DisplayName("06 - Saque com valor zero/negativo lança IllegalArgumentException")
    void sacar_ValorZeroOuNegativo_LancaIllegalArgumentException(double valor){
        // Arrange
        var conta = new Conta("Nicolas", 1000);

        // Act e Assert
        assertThrows(IllegalArgumentException.class, () -> conta.sacar(valor));
    }
  
    @Test
    @DisplayName("07 - Saque em conta inativa lança IllegalStateException")
    void sacar_ContaInativa_LancaIllegalStateException(){
        //Arrange:
        var conta = new Conta("Joana",0);
        conta.encerrar();

        //Act e Assert:
        assertThrows(IllegalStateException.class, () -> conta.sacar(100));       
     }
    
    // =======================================================
    //  Testes para transferir
    // =======================================================

    @Test
    @DisplayName("08 - Transferência válida atualiza saldo de ambas as contas")
    void transferir_ValorValido_AtualizaSaldoDeAmbasContas() {
        // Arrange:
        var origem = new Conta("Maria", 500);
        var destino = new Conta("João", 100);

        //Act:
        origem.transferir(destino, 100);

        //Assert:
        assertEquals(400, origem.getSaldo());
        assertEquals(200, destino.getSaldo());
    }

    @Test
    @DisplayName("09 - Transferência com saldo insuficiente lança exceção")
    void transferir_SaldoInsuficiente_LancaIllegalStateException(){
        // Arrange:
        var origem = new Conta("Maria", 0);
        var destino = new Conta("João", 100);

        //Act e Assert:
        assertThrows(IllegalStateException.class, () -> origem.transferir(destino, 100));   
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, -0.01, -20})
    @DisplayName("10 - Transferência com valor zero/negativo lança exceção")
    void transferir_ValorZeroOuNegativo_LancaIllegalArgumentException(double valor){
        // Arrange:
        var origem = new Conta("Maria", 500);
        var destino = new Conta("João", 100);

        //Act e Assert:
        assertThrows(IllegalArgumentException.class, () -> origem.transferir(destino, valor));
    }

    @Test
    @DisplayName("11 - Transferência com conta origem inativa lança exceção")
    void transferir_OrigemInativa_LancaIllegalStateException(){
        // Arrange:
        var origem = new Conta("Maria", 0);
        var destino = new Conta("João", 100);
        origem.encerrar();

        //Act e Assert:
        assertThrows(IllegalStateException.class, () -> origem.transferir(destino, 100));
    }

    @Test
    @DisplayName("12 - Transferência com conta destino inativa lança exceção")
    void transferir_DestinoInativa_LancaIllegalStateException(){
        // Arrange:
        var origem = new Conta("Maria", 500);
        var destino = new Conta("João", 0);
        destino.encerrar();

        //Act e Assert:
        assertThrows(IllegalStateException.class, () -> origem.transferir(destino, 100));
    }

    
    // =======================================================
    //  Testes para encerrar
    // =======================================================

    @Test
    @DisplayName("13 - Encerrar conta com saldo zero funciona")
    void encerrar_SaldoZero_Funciona(){
        // Arrange
        var conta = new Conta("Camila", 0);

        // Act
        conta.encerrar();
        
        // Assert
        assertFalse(conta.isAtiva()); //Conta encerrada tem isAtiva() == false
    }
 
    @Test
    @DisplayName("14 - Encerrar conta com saldo lança IllegalStateException")
    void encerrar_SaldoMaiorZero_LancaIllegalStateException(){
        // Arrange
        var conta = new Conta("Camila", 100);

        //Act e Assert:
        assertThrows(IllegalStateException.class, () -> conta.encerrar());  
    }

    @Test
    @DisplayName("15 - Encerrar conta já inativa lança IllegalStateException")
    void encerrar_ContaInativa_LancaIllegalStateException(){
        // Arrange
        var conta = new Conta("Camila", 0);
        conta.encerrar();

        //Act e Assert:
        assertThrows(IllegalStateException.class, () -> conta.encerrar());  
    }    

}
