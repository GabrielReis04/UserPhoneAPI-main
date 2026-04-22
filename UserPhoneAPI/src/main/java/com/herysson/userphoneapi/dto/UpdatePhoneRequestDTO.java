package com.herysson.userphoneapi.dto;

/**
 * Exercício 7 (Avançado) – DTO de entrada para atualização de telefone
 *
 * Separei o DTO de update do de criação porque em uma atualização
 * pode fazer sentido deixar campos opcionais (null = não alterar).
 * Aqui o id vem pela URL (PathVariable), então não precisa estar
 * no body — isso é uma boa prática REST.
 *
 * Usando record mesmo para update porque o record garante imutabilidade:
 * os dados chegam, são lidos, e a entidade é atualizada — sem risco
 * de alguém modificar o DTO no meio do processo.
 */
public record UpdatePhoneRequestDTO(
        String number,
        String type
) {}
