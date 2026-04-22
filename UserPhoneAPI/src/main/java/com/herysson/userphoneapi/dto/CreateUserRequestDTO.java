package com.herysson.userphoneapi.dto;

import java.util.List;

/**
 * DTO de entrada para criação de um novo usuário
 *
 * Permite cadastrar o usuário já com uma lista de telefones
 * (Exercício 5). Cada telefone vem como CreatePhoneRequestDTO,
 * mantendo o padrão de nunca deixar a entidade JPA entrar
 * diretamente pelo corpo da requisição.
 */
public record CreateUserRequestDTO(
        String name,
        String email,
        List<CreatePhoneRequestDTO> phones
) {}
