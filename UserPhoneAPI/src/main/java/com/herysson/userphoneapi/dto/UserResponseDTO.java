package com.herysson.userphoneapi.dto;

import java.util.List;

/**
 * DTO de saída para User
 *
 * Retorna os dados do usuário junto com a lista de telefones
 * já convertida para PhoneResponseDTO — sem expor a entidade
 * e sem o risco de loop infinito que existia antes com @JsonManagedReference.
 *
 * A senha não aparece aqui por motivos óbvios de segurança.
 */
public record UserResponseDTO(
        Long id,
        String name,
        String email,
        List<PhoneResponseDTO> phones
) {}
