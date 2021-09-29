package mx.edu.utez.restjava2.model;

import mx.edu.utez.restjava2.conexion.Conexion;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {
    private Connection con;
    private CallableStatement pstm;
    private ResultSet rs;

    public List<Customer> findAll(){
        List<Customer> listCustomers = new ArrayList<>();

        try{
            con = Conexion.getConnection();
            pstm = con.prepareCall("SELECT * FROM customers;");
            rs = pstm.executeQuery();

            while(rs.next()){
                Customer customer = new Customer();

                customer.setCustomerNumber(rs.getInt("customerNumber"));
                customer.setCustomerName(rs.getString("customerName"));

                listCustomers.add(customer);
            }
        }catch(SQLException e){
            System.out.printf("Ha sucedido algún error: %s", e.getMessage());
        }finally{
            try {
                rs.close();
                pstm.close();
                con.close();
            }catch (SQLException e){
                System.out.println("Error al cerrar conexiones");
            }
        }
        return listCustomers;
    }

    public Customer findByCustomerNumber(int customerNumber){
        Customer customer = null;

        try{
            con = Conexion.getConnection();
            pstm = con.prepareCall("SELECT * FROM customers WHERE customerNumber = ?;");
            pstm.setInt(1, customerNumber);
            rs = pstm.executeQuery();

            if(rs.next()){
                customer = new Customer();

                customer.setCustomerNumber(rs.getInt("customerNumber"));
                customer.setCustomerName(rs.getString("customerName"));
            }
        }catch(SQLException e){
            System.out.printf("Ha sucedido algún error: %s", e.getMessage());
        }finally{
            try {
                rs.close();
                pstm.close();
                con.close();
            }catch (SQLException e){
                System.out.println("Error al cerrar conexiones");
            }
        }
        return customer;
    }

    public boolean save(Customer customer, boolean isCreate){
        boolean flag = false;

        try{
            con = Conexion.getConnection();
            if(isCreate){
                pstm = con.prepareCall("INSERT INTO customers(customerNumber, customerName, contactLastName, contactFirstName, phone, addressLine1, addressLine2, city, state, postalCode, country, salesRepEmployeeNumber, creditLimit" +
                        ")VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?);");

                pstm.setInt(1, customer.getCustomerNumber());
                pstm.setString(2, customer.getCustomerName());
                pstm.setString(3, customer.getContactLastName());
                pstm.setString(4, customer.getContactFirstName());
                pstm.setString(5, customer.getPhone());
                pstm.setString(6, customer.getAddressLine1());
                pstm.setString(7, customer.getAddressLine2());
                pstm.setString(8, customer.getCity());
                pstm.setString(9, customer.getState());
                pstm.setString(10, customer.getPostalCode());
                pstm.setString(11, customer.getCountry());
                pstm.setInt(12, customer.getSalesRepEmployeeNumber());
                pstm.setDouble(13, customer.getCreditLimit());
            } else {
                pstm = con.prepareCall("UPDATE customers SET customerName = ?, contactLastName = ?, contactFirstName = ?, phone = ?, addressLine1 = ?, addressLine2 = ?, " +
                        "city = ?, state = ?, postalCode = ?, country = ?, salesRepEmployeeNumber = ?, creditLimit = ? WHERE customerNumber = ?;");

                pstm.setString(1, customer.getCustomerName());
                pstm.setString(2, customer.getContactLastName());
                pstm.setString(3, customer.getContactFirstName());
                pstm.setString(4, customer.getPhone());
                pstm.setString(5, customer.getAddressLine1());
                pstm.setString(6, customer.getAddressLine2());
                pstm.setString(7, customer.getCity());
                pstm.setString(8, customer.getState());
                pstm.setString(9, customer.getPostalCode());
                pstm.setString(10, customer.getCountry());
                pstm.setInt(11, customer.getSalesRepEmployeeNumber());
                pstm.setDouble(12, customer.getCreditLimit());
            }

            flag = pstm.executeUpdate() == 1;
        }catch(SQLException e){
            System.out.printf("Ha sucedido algún error: %s", e.getMessage());
        }finally{
            try {
                rs.close();
                pstm.close();
                con.close();
            }catch (SQLException e){
                System.out.println("Error al cerrar conexiones");
            }
        }
        return flag;
    }

    public boolean delete(int customerNumber){
        boolean flag = false;

        try{
            con = Conexion.getConnection();
            pstm = con.prepareCall("DELETE FROM customers WHERE customerNumber = ?;");
            pstm.setInt(1, customerNumber);
            flag = pstm.executeUpdate() == 1;
        }catch(SQLException e){
            System.out.printf("Ha sucedido algún error: %s", e.getMessage());
        }finally{
            try {
                rs.close();
                pstm.close();
                con.close();
            }catch (SQLException e){
                System.out.println("Error al cerrar conexiones");
            }
        }
        return flag;
    }
}
