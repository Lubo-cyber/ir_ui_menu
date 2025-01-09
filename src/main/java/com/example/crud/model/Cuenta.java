package com.example.crud.model;

import java.time.LocalDate;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Clase que representa una Cuenta con propiedades observables para usar en JavaFX.
 */
public class Cuenta {

  /**
   * Identificador único de la cuenta.
   */
  private final IntegerProperty id;

  /**
   * Secuencia o número asociado a la cuenta.
   */
  private final IntegerProperty sequence;

  /**
   * Indica si la cuenta está activa.
   */
  private final BooleanProperty active;

  /**
   * Fecha de creación de la cuenta.
   */
  private final ObjectProperty<LocalDate> createdate;

  /**
   * Nombre asociado a la cuenta.
   */
  private final StringProperty name;

  /**
   * Constructor que inicializa todas las propiedades de la cuenta.
   *
   * @param id         Identificador único de la cuenta.
   * @param sequence   Secuencia o número asociado a la cuenta.
   * @param active     Estado de actividad de la cuenta.
   * @param createDate Fecha de creación de la cuenta.
   * @param name       Nombre de la cuenta.
   */
  public Cuenta(Integer id, Integer sequence, Boolean active, LocalDate createDate, String name) {
    this.id = new SimpleIntegerProperty(id);
    this.sequence = new SimpleIntegerProperty(sequence);
    this.active = new SimpleBooleanProperty(active);
    this.createdate = new SimpleObjectProperty<>(createDate);
    this.name = new SimpleStringProperty(name);
  }

  /**
   * Obtiene el identificador único de la cuenta.
   *
   * @return El identificador de la cuenta.
   */
  public Integer getId() {
    return id.get();
  }

  /**
   * Establece el identificador único de la cuenta.
   *
   * @param id El nuevo identificador de la cuenta.
   */
  public void setId(Integer id) {
    this.id.set(id);
  }

  /**
   * Propiedad observable del identificador de la cuenta.
   *
   * @return La propiedad del identificador.
   */
  public IntegerProperty idProperty() {
    return id;
  }

  /**
   * Obtiene la secuencia o número asociado a la cuenta.
   *
   * @return La secuencia de la cuenta.
   */
  public Integer getSequence() {
    return sequence.get();
  }

  /**
   * Establece la secuencia o número asociado a la cuenta.
   *
   * @param sequence La nueva secuencia de la cuenta.
   */
  public void setSequence(Integer sequence) {
    this.sequence.set(sequence);
  }

  /**
   * Propiedad observable de la secuencia de la cuenta.
   *
   * @return La propiedad de la secuencia.
   */
  public IntegerProperty sequenceProperty() {
    return sequence;
  }

  /**
   * Obtiene el estado de actividad de la cuenta.
   *
   * @return {@code true} si la cuenta está activa, de lo contrario {@code false}.
   */
  public Boolean getActive() {
    return active.get();
  }

  /**
   * Establece el estado de actividad de la cuenta.
   *
   * @param active El nuevo estado de actividad de la cuenta.
   */
  public void setActive(Boolean active) {
    this.active.set(active);
  }

  /**
   * Propiedad observable del estado de actividad de la cuenta.
   *
   * @return La propiedad de actividad.
   */
  public BooleanProperty activeProperty() {
    return active;
  }

  /**
   * Obtiene la fecha de creación de la cuenta.
   *
   * @return La fecha de creación de la cuenta.
   */
  public LocalDate getCreatedate() {
    return createdate.get();
  }

  /**
   * Establece la fecha de creación de la cuenta.
   *
   * @param createdate La nueva fecha de creación de la cuenta.
   */
  public void setCreatedate(LocalDate createdate) {
    this.createdate.set(createdate);
  }

  /**
   * Propiedad observable de la fecha de creación de la cuenta.
   *
   * @return La propiedad de la fecha de creación.
   */
  public ObjectProperty<LocalDate> createDateProperty() {
    return createdate;
  }

  /**
   * Obtiene el nombre asociado a la cuenta.
   *
   * @return El nombre de la cuenta.
   */
  public String getName() {
    return name.get();
  }

  /**
   * Establece el nombre asociado a la cuenta.
   *
   * @param name El nuevo nombre de la cuenta.
   */
  public void setName(String name) {
    this.name.set(name);
  }

  /**
   * Propiedad observable del nombre de la cuenta.
   *
   * @return La propiedad del nombre.
   */
  public StringProperty nameProperty() {
    return name;
  }
}


