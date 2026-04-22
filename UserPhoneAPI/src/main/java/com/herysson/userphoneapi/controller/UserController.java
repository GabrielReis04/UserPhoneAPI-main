package com.herysson.userphoneapi.controller;

import com.herysson.userphoneapi.dto.CreateUserRequestDTO;
import com.herysson.userphoneapi.dto.UpdatePhoneRequestDTO;
import com.herysson.userphoneapi.dto.UserResponseDTO;
import com.herysson.userphoneapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Exercício 4 – Controller atualizado para usar DTOs
 *
 * Antes desse exercício o controller estava injetando o UserRepository
 * diretamente e retornando a entidade User — o que é uma má prática
 * porque expõe a estrutura interna do banco para o cliente.
 *
 * Agora o controller só conhece DTOs e o UserService.
 * Toda a lógica de negócio foi movida para o service (Exercício 5),
 * e o mapeamento entre entidade e DTO está no mapper (Exercício 3).
 * Aqui o controller só tem responsabilidade de HTTP:
 *   - receber a requisição
 *   - chamar o service
 *   - devolver a resposta com o status correto
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // Injeção pelo construtor — mais segura que @Autowired no campo
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /users
     * Retorna todos os usuários já no formato DTO, sem expor a entidade.
     */
    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * GET /users/{id}
     * Busca um usuário pelo id. Se não existir, retorna 404.
     *
     * O Optional vem do service — o controller decide o que fazer
     * com ele (virar 200 ou 404), que é responsabilidade HTTP.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /users
     * Cria um novo usuário recebendo um CreateUserRequestDTO no body.
     *
     * Exercício 5 – O DTO de entrada aceita uma lista de telefones
     * para já cadastrar tudo de uma vez, seguindo o que o professor pediu.
     *
     * Retorna 201 Created com o UserResponseDTO do usuário salvo.
     * Usei ResponseEntity.status(201) em vez de @ResponseStatus para
     * manter o padrão de retornar ResponseEntity nos outros métodos.
     */
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody CreateUserRequestDTO dto) {
        UserResponseDTO created = userService.createUser(dto);
        return ResponseEntity.status(201).body(created);
    }

    /**
     * DELETE /users/{id}
     * Remove um usuário pelo id. Retorna 204 se deletou, 404 se não achou.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * PUT /users/{userId}/phones/{phoneId}
     * Exercício 7 (Avançado) – Atualização de telefone com DTO de entrada
     *
     * O id do usuário e do telefone vêm pela URL (PathVariable),
     * que é o jeito REST correto — o body só traz os dados a atualizar.
     *
     * O service valida se o telefone pertence ao usuário antes de atualizar,
     * evitando que alguém atualize o telefone de outro usuário passando
     * um phoneId qualquer.
     *
     * Retorna o UserResponseDTO atualizado para que o cliente veja
     * o estado completo do usuário após a mudança.
     */
    @PutMapping("/{userId}/phones/{phoneId}")
    public ResponseEntity<UserResponseDTO> updatePhone(
            @PathVariable Long userId,
            @PathVariable Long phoneId,
            @RequestBody UpdatePhoneRequestDTO dto) {

        return userService.updatePhone(userId, phoneId, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
