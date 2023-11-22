package com.gadalos.planificacion_turismo_ia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private EditText edtUsuario, edtContrasena;
    private TextView tvRegistrese, tvOlvidoContrasena;
    private ImageView ivLogoGoogle;
    private Button btnIniciarSesion;
    private FirebaseFirestore mFirestore;
    public FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        edtContrasena = (EditText) findViewById(R.id.edtContrasena);
        btnIniciarSesion = (Button) findViewById(R.id.btnIniciar);
        tvRegistrese = (TextView) findViewById(R.id.tvRegistrese);
        tvOlvidoContrasena = (TextView) findViewById(R.id.tvOlvidoContrasena);
        ivLogoGoogle = (ImageView) findViewById(R.id.ivLogoGoogle);

        mAuth = FirebaseAuth.getInstance();
        tvRegistrese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Acceder al Registro
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tvOlvidoContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Acceder a Recuperar Contraseña
                Intent intent = new Intent(LoginActivity.this, RecuperarContrasenaActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });

        ivLogoGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END config_signin]

        // [START initialize_auth]
        // Initialize Firebase Auth

        // [END initialize_auth]
    }

    // [START on_start_check_user]
    /*@Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }*/
    // [END on_start_check_user]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Llamar a la función para procesar los datos del usuario
                            handleGoogleSignIn(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

    // Después de obtener los datos del usuario en handleGoogleSignIn o donde sea apropiado
    private void handleGoogleSignIn(FirebaseUser user) {
        if (user != null) {
            // Obtener detalles específicos de Google
            String name = user.getDisplayName();
            String email = user.getEmail();
            String cel = user.getPhoneNumber();
            Uri photoUrl = user.getPhotoUrl();

            // Log para verificar si los datos de Google son correctos
            Log.d(TAG, "Nombre: " + name);
            Log.d(TAG, "Correo: " + email);
            Log.d(TAG, "Celular: " + cel);
            Log.d(TAG, "Foto URL: " + (photoUrl != null ? photoUrl.toString() : "N/A"));

            // Crear un Intent
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

            // Agregar datos al Bundle
            Bundle bundle = new Bundle();
            bundle.putString("nombre", name);
            bundle.putString("correo", email);
            bundle.putString("celular", cel != null ? cel : "");
            bundle.putString("foto", photoUrl != null ? photoUrl.toString() : "");

            // Agregar el Bundle al Intent
            intent.putExtras(bundle);

            // Registrar automáticamente en Firestore
            registrarUsuarioFirestore(name, email, cel != null ? cel : "", photoUrl != null ? photoUrl.toString() : "");

            // Iniciar la nueva actividad
            startActivity(intent);
            finish();
            Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
        }
    }

    private void registrarUsuarioFirestore(String nombre, String correo, String celular, String foto) {
        // Obtener el ID único del usuario actual
        String id = mAuth.getCurrentUser().getUid();

        // Crear un mapa con los datos del usuario
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("nombre", nombre);
        usuario.put("correo", correo);
        usuario.put("celular", celular);
        usuario.put("foto", foto);

        // Log para verificar los datos antes de enviar a Firestore
        Log.d(TAG, "Registrando usuario en Firestore - ID: " + id);
        Log.d(TAG, "Datos del usuario: " + usuario.toString());

        // Agregar el usuario a la colección "usuario" en Firestore
        mFirestore.collection("usuario").document(id)
                .set(usuario)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Usuario registrado en Firestore");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error al registrar usuario en Firestore", e);
                    }
                });
    }

    // [END auth_with_google]

    // [START signin]
    private void login() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish(); // Esta línea asegura que la actividad actual se cierre después de iniciar la nueva actividad.
            Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
        }
    }

    public void iniciarSesion(){
        //Convertimos a String debido a que debedemos comprobar si estan vacios los campos
        String username = edtUsuario.getText().toString().trim();
        String password = edtContrasena.getText().toString().trim();
        if (username.isEmpty() || password.isEmpty()) {
            //Mostramos un mensaje para ver que se llenen los campos
            Toast.makeText(LoginActivity.this, "Complete los campos vacíos", Toast.LENGTH_SHORT).show();
        } else {
            iniciarUsuario(username, password);
//            Toast.makeText(LoginActivity.this, "Prueba nuevo", Toast.LENGTH_SHORT).show();
        }
    }

    private void iniciarUsuario(String username, String password) {
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null && user.isEmailVerified()) {
                                // Verificar si la dirección de correo electrónico está verificada
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                // Si el correo electrónico no está verificado, mostrar un mensaje
                                Toast.makeText(LoginActivity.this, "Verifica tu dirección de correo electrónico antes de iniciar sesión", Toast.LENGTH_SHORT).show();
                                mAuth.signOut(); // Cerrar sesión si el correo electrónico no está verificado
                            }
                        } else {
                            // Si el inicio de sesión falla, muestra un mensaje al usuario.
                            Toast.makeText(LoginActivity.this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //Si la cuenta ya esta iniciada pasar sin iniciar sesion
    /*@Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    }*/
}