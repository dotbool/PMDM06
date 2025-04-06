package martinezruiz.javier.pmdm06.models;

/**
 * Esta clase maneja errores en los viewModel.
 * Si hay un cambio de configuración, el LiveData volvería a informar
 * sobre el error, ya que las instancias sobreviven a los cambios de configuración.
 * Para evitar que un error vuelva a comunicarse, se utiliza la lógica de si ya ha sido
 * tratado a través de isHandled
 */
public class ErrorData {


    public ErrorData(String error) {
        this.error = error;
        this.handled = false;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Boolean isHandled() {
        return handled;
    }

    public void setHandled(Boolean handled) {
        this.handled = handled;
    }

    String error;
    Boolean handled;



}
