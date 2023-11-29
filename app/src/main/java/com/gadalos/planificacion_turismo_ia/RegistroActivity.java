package com.gadalos.planificacion_turismo_ia;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class RegistroActivity extends AppCompatActivity {

    private EditText edtUsuario2, edtCorreo, edtCelular, edtContrasena2, edtReContrasena;
    private TextView tvInicio2;
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
        tvInicio2 = (TextView) findViewById(R.id.tvInicio2);

        //Conexion con el Firestore
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        tvInicio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Acceder al Login
                Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validar();
            }
        });
    }

    public void validar() {
        //Convertimos a String debido a que debemos comprobar si están vacíos los campos
        String username = edtUsuario2.getText().toString().trim();
        String email = edtCorreo.getText().toString().trim();
        String phone = edtCelular.getText().toString().trim();
        String password = edtContrasena2.getText().toString().trim();
        String repassword = edtReContrasena.getText().toString().trim();

        // Expresión regular para validar una dirección de correo electrónico
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";

        // Expresión regular para validar un número de celular de 9 dígitos
        String celularPattern = "^[0-9]{9}$";

        // Validar que todos los campos estén llenos y que las contraseñas coincidan
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty() || repassword.isEmpty()) {
            // Mostrar un mensaje para que se llenen los campos
            Toast.makeText(RegistroActivity.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(repassword)) {
            // Mostrar un mensaje si las contraseñas no coinciden
            Toast.makeText(RegistroActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 8) { // Validar longitud mínima de contraseña (puedes ajustar esto)
            Toast.makeText(RegistroActivity.this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show();
        } else if (!email.matches(emailPattern)) {
            // Mostrar un mensaje si la dirección de correo electrónico no es válida
            Toast.makeText(RegistroActivity.this, "Correo electrónico no válido", Toast.LENGTH_SHORT).show();
        } else if (!phone.matches(celularPattern)) {
            Toast.makeText(RegistroActivity.this, "Se necesita solo 9 dígitos númericos", Toast.LENGTH_SHORT).show();
        } else {
            // Si pasa todas las validaciones, proceder a registrar el usuario
            registrarUsuario(username, email, phone, password);
            Toast.makeText(RegistroActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            // Limpiar los campos después del registro
            edtUsuario2.setText("");
            edtCorreo.setText("");
            edtCelular.setText("");
            edtContrasena2.setText("");
            edtReContrasena.setText("");
        }
    }

    private void registrarUsuario(String username, String email, String phone, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser(); // Obtener el usuario actual
                    if (user != null) {
                        enviarCorreoVerificacion(user); // Enviar correo de verificación
                        String id = user.getUid();
                        //String id = mAuth.getCurrentUser().getUid();
                        String hashedPassword = hashPassword(password); // Función para obtener el hash seguro

                        Map<String, Object> map = new HashMap<>();
                        map.put("id", id);
                        map.put("nombre", username);
                        map.put("correo", email);
                        map.put("celular", phone);
                        map.put("contrasena", hashedPassword); // Almacenar el hash en lugar de la contraseña en texto plano

                        mFirestore.collection("usuario").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
                                Toast.makeText(RegistroActivity.this, "El Usuario se ha registrado con éxito", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegistroActivity.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    // Manejar el fallo en el registro de usuario
                    Toast.makeText(RegistroActivity.this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String hashPassword(String password) {
        try {
            // Utilizar una función de hash segura como PBKDF2
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), "salt".getBytes(), 10000, 256);
            SecretKey secretKey = keyFactory.generateSecret(keySpec);
            byte[] hash = secretKey.getEncoded();
            return Base64.encodeToString(hash, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void enviarCorreoVerificacion(FirebaseUser user) {
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegistroActivity.this, "Se ha enviado un correo de verificación", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "Error al enviar el correo de verificación", task.getException());
                    }
                }
            });
        }
    }

}