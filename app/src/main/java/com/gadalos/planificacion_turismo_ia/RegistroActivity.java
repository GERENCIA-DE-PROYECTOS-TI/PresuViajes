package com.gadalos.planificacion_turismo_ia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegistroActivity extends AppCompatActivity {

    private EditText edtUsuario2, edtCorreo, edtCelular, edtContrasena2, edtReContrasena;
    private Button btnRegistrar;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        //Conectandos mediante los id
        edtUsuario2 = (EditText) findViewById(R.id.edtUsuario2);
        edtCorreo = (EditText) findViewById(R.id.edtCorreo);
        edtCelular = (EditText) findViewById(R.id.edtCelular);
        edtContrasena2 = (EditText) findViewById(R.id.edtContrasena2);
        edtReContrasena = (EditText) findViewById(R.id.edtReContrasena);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);

        //Conexion con el Firestore
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void registrar(){
        //Convertimos a String debido a que debedemos comprobar si estan vacios los campos
        String username = edtUsuario2.getText().toString().trim();
        String email = edtCorreo.getText().toString().trim();
        String phone = edtCelular.getText().toString().trim();
        String password = edtContrasena2.getText().toString().trim();
        String repassword = edtReContrasena.getText().toString().trim();
        //Validamos si todos los campos esta llenos
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty() || repassword.isEmpty()) {
            //Mostramos un mensaje para ver que se llenen los campos
            Toast.makeText(RegistroActivity.this, "Complete los campos vacios", Toast.LENGTH_SHORT).show();
        } else {
            if (password == repassword){
                registrarUsuario(username, email, phone, password);

            }else {
                Toast.makeText(RegistroActivity.this, "La contraseña ingresada no concuerda con el otro campo", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void registrarUsuario(String username, String email, String phone, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Map<String, Object> map = new HashMap<>();
                int id = 1;
                map.put("id", id );
                map.put("nombre", username);
                map.put("correo", email);
                map.put("telefono", phone);
                map.put("contrasena", password);

                mFirestore.collection("usuario").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        finish();
                        startActivity(new Intent(RegistroActivity.this, MainActivity.class));
                        Toast.makeText(RegistroActivity.this, "El Usuario se ha registrado con exito", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistroActivity.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}