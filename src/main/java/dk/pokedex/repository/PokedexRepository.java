package dk.pokedex.repository;

import dk.pokedex.model.Pokedex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PokedexRepository {

    //Database properties injected from application.properties
    @Value("${spring.datasource.url}")
    private String DB_URL;
    @Value("${spring.datasource.username}")
    private String UID;
    @Value("${spring.datasource.password}")
    private String PWD;

    public List<Pokedex> getAll(){
        List<Pokedex> pokedexList = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            Statement statement = connection.createStatement();
            final String SQL_QUERY = "SELECT * FROM pokemon";
            ResultSet resultSet = statement.executeQuery(SQL_QUERY);
            while(resultSet.next()) {
                int pokedex_number = resultSet.getInt(1);
                String name = resultSet.getString(2);
                int speed = resultSet.getInt(3);
                int special_defence = resultSet.getInt(4);
                int special_attack = resultSet.getInt(5);
                int defence = resultSet.getInt(6);
                int attack = resultSet.getInt(7);
                int hp = resultSet.getInt(8);
                String primary_type = resultSet.getString(9);
                String secondary_type = resultSet.getString(10);
                Pokedex pokedex = new Pokedex(pokedex_number, name, speed, special_defence, special_attack,
                    defence, attack, hp, primary_type, secondary_type);
                pokedexList.add(pokedex);

            }

        } catch(SQLException SQLE){
            System.out.println("There was no connection found.");
            SQLE.printStackTrace();
        }
        return pokedexList;
    }
    public void addPokemon(Pokedex pokedex){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);

            final String CREATE_QUERY = "INSERT INTO pokemon(pokedex_number, name, speed, special_defence," +
                    "special_attack, defence, attack, hp, primary_type, secondary_type) VALUES (?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_QUERY);

            //Set attributes in prepared statement
            preparedStatement.setInt(1, pokedex.getPokedex_number());
            preparedStatement.setString(2, pokedex.getName());
            preparedStatement.setInt(3, pokedex.getSpeed());
            preparedStatement.setInt(4, pokedex.getSpecial_defence());
            preparedStatement.setInt(5, pokedex.getSpecial_attack());
            preparedStatement.setInt(6, pokedex.getDefence());
            preparedStatement.setInt(7, pokedex.getAttack());
            preparedStatement.setInt(8, pokedex.getHp());
            preparedStatement.setString(9, pokedex.getPrimary_type());
            preparedStatement.setString(10, pokedex.getSecondary_type());

            preparedStatement.executeUpdate();

        }catch(SQLException SQLE){
            System.out.println("Could not add pokemon.");
            SQLE.printStackTrace();
        }
    }
    public void updatePokedex(Pokedex pokedex){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            final String UPDATE_QUERY = "UPDATE pokemon SET name = ?, speed = ?, special_defence = ?, " +
                                        "special_attack = ?, defence = ?, attack = ?, hp = ?, primary_type = ?, " +
                                        "secondary_type = ? WHERE pokedex_number = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            String name = pokedex.getName();
            int speed = pokedex.getSpeed();
            int special_defence = pokedex.getSpecial_defence();
            int special_attack = pokedex.getSpecial_attack();
            int defence = pokedex.getDefence();
            int attack = pokedex.getAttack();
            int hp = pokedex.getHp();
            String primary_type = pokedex.getPrimary_type();
            String secondary_type = pokedex.getSecondary_type();
            int pokedex_number = pokedex.getPokedex_number();

            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, speed);
            preparedStatement.setInt(3, special_defence);
            preparedStatement.setInt(4, special_attack);
            preparedStatement.setInt(5, defence);
            preparedStatement.setInt(6, attack);
            preparedStatement.setInt(7, hp);
            preparedStatement.setString(8, primary_type);
            preparedStatement.setString(9, secondary_type);
            preparedStatement.setInt(10, pokedex_number);

            preparedStatement.executeUpdate();

        }catch(SQLException SQLE){
            System.out.println("Could not update pokedex.");
            SQLE.printStackTrace();
        }
    }

    public Pokedex findPokemonByNumber(int pokedex_number) {
        Pokedex pokedex = new Pokedex();
        pokedex.setPokedex_number(pokedex_number);
        try{
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            final String FIND_POKEMON_QUERY = "SELECT * FROM pokemon WHERE pokedex_number = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_POKEMON_QUERY);

            preparedStatement.setInt(1, pokedex_number);

            //Make a result set for return statement
            ResultSet resultSet = preparedStatement.executeQuery();

            //Return result
            resultSet.next();

            String name = resultSet.getString(2);
            int speed = resultSet.getInt(3);
            int special_defence = resultSet.getInt(4);
            int special_attack = resultSet.getInt(5);
            int defence = resultSet.getInt(6);
            int attack = resultSet.getInt(7);
            int hp = resultSet.getInt(8);
            String primary_type = resultSet.getString(9);
            String secondary_type = resultSet.getString(10);

            pokedex.setName(name);
            pokedex.setSpeed(speed);
            pokedex.setSpecial_defence(special_defence);
            pokedex.setSpecial_attack(special_attack);
            pokedex.setDefence(defence);
            pokedex.setAttack(attack);
            pokedex.setHp(hp);
            pokedex.setPrimary_type(primary_type);
            pokedex.setSecondary_type(secondary_type);

        }catch(SQLException SQLE){
            System.out.println("Could not find pokemon.");
            SQLE.printStackTrace();
        }
        return pokedex;
    }

    public void deletePokemonNumber(int pokedex_number) {
        try{
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            final String DELETE_POKEMON = "DELETE FROM pokemon WHERE pokedex_number = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_POKEMON);
            preparedStatement.setInt(1, pokedex_number);

            preparedStatement.executeUpdate();

        }catch(SQLException SQLE){
            System.out.println("Could not delete pokemon.");
            SQLE.printStackTrace();
        }
    }
}
