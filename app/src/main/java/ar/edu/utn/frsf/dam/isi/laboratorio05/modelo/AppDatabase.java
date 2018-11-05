package ar.edu.utn.frsf.dam.isi.laboratorio05.modelo;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Reclamo.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ReclamoDao reclamoDao();
}
