package Controller;

import Modelo.RepresentacionIEEE;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;

public class RepresentacionController {

    private final RepresentacionIEEE rep = new RepresentacionIEEE();

    // Componentes FXML inyectados
    @FXML
    private ComboBox<String> cBoxBitsD, cBoxBitsI;
    @FXML
    private Button cmdIEEEtoDec, cmdDecToIEEE1;
    @FXML
    private Label lblExponente, lblExponente1, lblMantisa, lblMantisa1, lblSigno, lblSigno1;
    @FXML
    private TextField txtNumero, txtNumero1, txtExponente, txtMantisa, txtSigno;

    @FXML
    public void handleConversion() {
        if (cBoxBitsD.getValue().equals("32 bits")) {
            decToIEEE32();
        } else {
            decToIEEE64();
        }
    }

    @FXML
    public void handleConversion2() {
        if (cBoxBitsI.getValue().equals("32 bits")) {
            IEEEToDec32();
        } else {
            IEEEToDec64();
        }
    }

    @FXML
    public void IEEEToDec64() {
        try {
            String signo = txtSigno.getText().trim();
            String exponente = txtExponente.getText().trim();
            String mantisa = txtMantisa.getText().trim();

            validarCamposIEEE64(signo, exponente, mantisa);

            String ieee = signo + exponente + mantisa;
            double dec = rep.IEEEToDec64(ieee);
            txtNumero.setText(String.format("%.15f", dec));

        } catch (IllegalArgumentException e) {
            mostrarAlerta("Error en formato IEEE 64 bits", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta("Error crítico", "Error en conversión: " + e.getMessage());
        }
    }

    private void validarCamposIEEE64(String signo, String exponente, String mantisa) {
        if (signo.isEmpty() || exponente.isEmpty() || mantisa.isEmpty()) {
            throw new IllegalArgumentException("Todos los campos deben estar completos");
        }
        if (signo.length() != 1 || !signo.matches("[01]")) {
            throw new IllegalArgumentException("Signo debe ser 1 bit (0 o 1)");
        }
        if (exponente.length() != 11 || !exponente.matches("[01]+")) {
            throw new IllegalArgumentException("Exponente debe tener 11 bits binarios");
        }
        if (mantisa.length() != 52 || !mantisa.matches("[01]+")) {
            throw new IllegalArgumentException("Mantisa debe tener 52 bits binarios");
        }
    }

    // Conversión Decimal a IEEE 64 bits
    @FXML
    public void decToIEEE64() {
        try {
            double decimal = Double.parseDouble(txtNumero1.getText());
            String resultado = rep.decToIEEE64(decimal);

            lblSigno1.setText(resultado.substring(0, 1));
            lblExponente1.setText(resultado.substring(1, 12));  // 11 bits exponente
            lblMantisa1.setText(resultado.substring(12));       // 52 bits mantisa

        } catch (NumberFormatException e) {
            mostrarAlerta("Error numérico", "Ingrese un número válido");
        } catch (Exception e) {
            mostrarAlerta("Error de conversión", e.getMessage());
        }
    }

    // Métodos comunes
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void validarEntradaBinaria(String newVal, int maxLength, TextField campo) {
        if (!newVal.matches("[01]*")) { // Solo permite 0s y 1s
            campo.setText(newVal.replaceAll("[^01]", ""));
        }
        if (newVal.length() > maxLength) { // Limita la longitud
            campo.setText(newVal.substring(0, maxLength));
        }
    }

    // Métodos para 32 bits (mantenidos de tu versión original)
    // Conversión IEEE 32 bits a Decimal
    @FXML
    public void IEEEToDec32() {
        try {
            String signo = txtSigno.getText().trim();
            String exponente = txtExponente.getText().trim();
            String mantisa = txtMantisa.getText().trim();

            validarCamposIEEE32(signo, exponente, mantisa);

            String ieee = signo + exponente + mantisa;
            double dec = rep.IEEEToDec32(ieee);
            txtNumero.setText(String.format("%.15f", dec));

        } catch (IllegalArgumentException e) {
            mostrarAlerta("Error en formato IEEE 32 bits", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta("Error crítico", "Error en conversión: " + e.getMessage());
        }
    }

    private void validarCamposIEEE32(String signo, String exponente, String mantisa) {
        if (signo.isEmpty() || exponente.isEmpty() || mantisa.isEmpty()) {
            throw new IllegalArgumentException("Complete todos los campos");
        }
        if (signo.length() != 1 || !signo.matches("[01]")) {
            throw new IllegalArgumentException("Signo: 1 bit (0/1)");
        }
        if (exponente.length() != 8 || !exponente.matches("[01]+")) {
            throw new IllegalArgumentException("Exponente: 8 bits binarios");
        }
        if (mantisa.length() != 23 || !mantisa.matches("[01]+")) {
            throw new IllegalArgumentException("Mantisa: 23 bits binarios");
        }
    }

    // Conversión Decimal a IEEE 32 bits
    @FXML
    public void decToIEEE32() {
        try {
            double decimal = Double.parseDouble(txtNumero1.getText());
            String resultado = rep.decToIEEE32(decimal);

            lblSigno1.setText(resultado.substring(0, 1));
            lblExponente1.setText(resultado.substring(1, 9));   // 8 bits exponente
            lblMantisa1.setText(resultado.substring(9));        // 23 bits mantisa

        } catch (NumberFormatException e) {
            mostrarAlerta("Error numérico", "Ingrese un número válido");
        } catch (Exception e) {
            mostrarAlerta("Error de conversión", e.getMessage());
        }
    }

    @FXML
    void initialize() {
        cBoxBitsD.getItems().addAll("32 bits", "64 bits");
        cBoxBitsD.setValue("32 bits");
        cBoxBitsI.getItems().addAll("32 bits", "64 bits");
        cBoxBitsI.setValue("32 bits");

        txtSigno.textProperty().addListener((obs, oldVal, newVal)
                -> validarEntradaBinaria(newVal, 1, txtSigno));

        txtExponente.textProperty().addListener((obs, oldVal, newVal) -> {
            int maxBits = cBoxBitsI.getValue().equals("32 bits") ? 8 : 11;
            validarEntradaBinaria(newVal, maxBits, txtExponente);
        });

        txtMantisa.textProperty().addListener((obs, oldVal, newVal) -> {
            int maxBits = cBoxBitsI.getValue().equals("32 bits") ? 23 : 52;
            validarEntradaBinaria(newVal, maxBits, txtMantisa);
        });

        cBoxBitsI.valueProperty().addListener((obs, oldVal, newVal) -> {
            int mantisaBits = newVal.equals("32 bits") ? 23 : 52;
            validarEntradaBinaria(txtMantisa.getText(), mantisaBits, txtMantisa);
        });
        cmdDecToIEEE1.setOnAction(e -> handleConversion());
        cmdIEEEtoDec.setOnAction(e -> handleConversion2());
    }

}
