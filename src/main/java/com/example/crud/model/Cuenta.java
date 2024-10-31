package com.example.crud.model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Cuenta {
    private Integer ID;
    private Integer SEQUENCE;
    private Boolean ACTIVE;
    private Date CREATE_DATE;
    private String NAME;

    public Cuenta(Integer id, Integer sequence, Boolean active, Date createDate, String name) {
        this.ID = id;
        this.SEQUENCE = sequence;
        this.ACTIVE = active;
        this.CREATE_DATE = createDate;
        this.NAME = name;
    }

    public static List<Cuenta> buscarPorId(Integer id) throws SQLException {
        List<Cuenta> cuentas = new ArrayList<>();
        String sql = "SELECT * FROM ir_ui_menu WHERE id = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement pst = conexion.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Integer cuentaId = rs.getInt("id");
                Integer sequence = rs.getInt("sequence");
                Boolean active = rs.getBoolean("active");
                Date createDate = rs.getDate("create_date");
                String name = rs.getString("name");

                // Creamos una instancia de Cuenta con los datos obtenidos
                Cuenta cuenta = new Cuenta(cuentaId, sequence, active, createDate, name);
                cuentas.add(cuenta); // Añadimos a la lista
            }
        }

        return cuentas;
    }


    public String getNAME() {
        return NAME;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getSEQUENCE() {
        return SEQUENCE;
    }

    public void setSEQUENCE(Integer SEQUENCE) {
        this.SEQUENCE = SEQUENCE;
    }

    public Boolean getACTIVE() {
        return ACTIVE;
    }

    public void setACTIVE(Boolean ACTIVE) {
        this.ACTIVE = ACTIVE;
    }

    public Date getCREATE_DATE() {
        return CREATE_DATE;
    }

    public void setCREATE_DATE(Date CREATE_DATE) {
        this.CREATE_DATE = CREATE_DATE;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }
}
