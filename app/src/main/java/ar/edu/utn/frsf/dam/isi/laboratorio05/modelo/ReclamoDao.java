package ar.edu.utn.frsf.dam.isi.laboratorio05.modelo;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ReclamoDao {
    @Query("SELECT * FROM Reclamo")
    List<Reclamo> getAll();

    @Insert
    long insert(Reclamo r);

    @Insert
    void update(Reclamo r);

    @Delete
    void delete(Reclamo r);
}