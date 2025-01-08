package com.example.crud.model;

import com.example.crud.model.Conexion;
import com.example.crud.model.Cuenta;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class IrUiMenu {
    private Cuenta cuenta;

    public IrUiMenu(int id, int secuencia, boolean active, LocalDate createDate, String name) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);


        this.cuenta = new Cuenta(id, secuencia, active, createDate, jsonObject.toString());
    }

    public void meter() throws SQLException {
        String sql = "INSERT INTO ir_ui_menu (id, sequence, active, create_date, name) VALUES (?,?,?,?,?)";
        try (Connection conectar = Conexion.conectar();
             PreparedStatement pst = conectar.prepareStatement(sql)) {


            pst.setInt(1, cuenta.getID());
            pst.setInt(2, cuenta.getSEQUENCE());
            pst.setBoolean(3, cuenta.getACTIVE());
            pst.setDate(4, java.sql.Date.valueOf(cuenta.getCREATE_DATE()));


            pst.setString(5, cuenta.getNAME());

            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}

