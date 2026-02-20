package com.chakray.exercise.controller;

import com.chakray.exercise.dto.LoginRequest;
import com.chakray.exercise.model.Address;
import com.chakray.exercise.model.User;
import com.chakray.exercise.util.EncryptionUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuarios", description = "Operaciones de gestión de usuarios para el examen")
public class UserController {
    private final List<User> users = new ArrayList<>();

    @PostConstruct
    public void init() {

        users.add(User.builder().id(UUID.randomUUID()).email("user1@mail.com").name("user1").phone("+1 55 555 555 55").password("7c4a8d09ca3762af61e59520943dc26494f8941b").tax_id("AARR990101XXX").created_at("01-01-2026 00:00:00").addresses(List.of(Address.builder().id(1).name("workaddress").street("street No. 1").country_code("UK").build(), Address.builder().id(2).name("homeaddress").street("street No. 2").country_code("AU").build())).build());

        users.add(User.builder().id(UUID.randomUUID()).email("admin@mail.com").name("admin_user").phone("+34 600 000 000").password("b7a87c... (hash)").tax_id("BBSS880202YYY").created_at("15-01-2026 10:00:00").addresses(List.of(Address.builder().id(3).name("main_office").street("Av. Diagonal 123").country_code("ES").build())).build());

        users.add(User.builder().id(UUID.randomUUID()).email("tester@mail.com").name("test_account").phone("+52 55 1122 3344").password("f1e2d3... (hash)").tax_id("CCTT770303ZZZ").created_at("10-02-2026 15:30:00").addresses(List.of(Address.builder().id(4).name("warehouse").street("Industrial Park 5").country_code("MX").build())).build());
    }

    @Operation(
            summary = "Consultar usuarios",
            description = "Permite obtener todos los usuarios, filtrarlos y ordenardos."
    )
    @GetMapping
    public List<User> getUsers(
            @Parameter(description = "Atributo para ordenar", example = "name")
            @RequestParam(required = false) String sortedBy,

            @Parameter(description = "Filtro: campo+op+valor", example = "name+co+user")
            @RequestParam(required = true) String filter) {

        List<User> result = new ArrayList<>(users);


        if (filter != null && !filter.isBlank()) {
            String[] parts = filter.split("[\\+ ]");
            if (parts.length >= 3) {
                result = result.stream()
                        .filter(u -> applyFilter(u, parts[0], parts[1], parts[2]))
                        .collect(Collectors.toList());
            }
        }


        if (sortedBy != null && !sortedBy.isBlank()) {
            result.sort((u1, u2) -> {
                String val1 = getFieldValue(u1, sortedBy);
                String val2 = getFieldValue(u2, sortedBy);
                return val1.compareToIgnoreCase(val2);
            });
        }

        return result;
    }

    @Operation(
            summary = "Crear usuario",
            description = "Guarda un usuario validando el formato de RFC, teléfono (Andres Format) y que el tax_id sea único."
    )
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User newUser) {

        boolean isDuplicate = users.stream()
                .anyMatch(existingUser -> existingUser.getTax_id().equalsIgnoreCase(newUser.getTax_id()));
        if (isDuplicate) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: El tax_id '" + newUser.getTax_id() + "' ya se encuentra registrado.");
        }

        String encrypted = EncryptionUtil.encrypt(newUser.getPassword());
        newUser.setPassword(encrypted);

        newUser.setId(UUID.randomUUID());
        newUser.setCreated_at(java.time.ZonedDateTime.now(java.time.ZoneId.of("Indian/Antananarivo"))
                .format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));

        users.add(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @Operation(summary = "Actualizar atributo por ID", description = "Modifica campos específicos de un usuario.")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody User updates) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .map(user -> {
                    if (updates.getName() != null) user.setName(updates.getName());
                    if (updates.getEmail() != null) user.setEmail(updates.getEmail());
                    if (updates.getPhone() != null) user.setPhone(updates.getPhone());
                    if (updates.getTax_id() != null) user.setTax_id(updates.getTax_id());
                    return ResponseEntity.ok(user);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar usuario", description = "Borra un usuario de la lista por su UUID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        boolean removed = users.removeIf(u -> u.getId().equals(id));
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Autenticación de usuario",
            description = "Login"
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginData) {
        String hashedPassword = EncryptionUtil.encrypt(loginData.password());

        return users.stream()
                .filter(u -> u.getTax_id().equalsIgnoreCase(loginData.username())
                        && hashedPassword.equals(u.getPassword()))
                .findFirst()
                .map(user -> ResponseEntity.ok("¡Acceso concedido! Bienvenido " + user.getName()))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("RFC o contraseña incorrectos."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Método separado para la lógica de comparación
     */
    private boolean applyFilter(User user, String field, String operator, String value) {
        String fieldValue = getFieldValue(user, field).toLowerCase();
        String searchVal = value.toLowerCase();

        return switch (operator) {
            case "eq" -> fieldValue.equals(searchVal);
            case "co" -> fieldValue.contains(searchVal);
            case "sw" -> fieldValue.startsWith(searchVal);
            case "ew" -> fieldValue.endsWith(searchVal);
            default -> true;
        };
    }

    private String getFieldValue(User u, String attribute) {
        if (u == null) return "";

        return switch (attribute) {
            case "email" -> u.getEmail() != null ? u.getEmail() : "";
            case "id" -> u.getId() != null ? u.getId().toString() : "";
            case "name" -> u.getName() != null ? u.getName() : "";
            case "phone" -> u.getPhone() != null ? u.getPhone() : "";
            case "tax_id" -> u.getTax_id() != null ? u.getTax_id() : "";
            case "created_at" -> u.getCreated_at() != null ? u.getCreated_at() : "";
            default -> "";
        };
    }
}
