package com.herysson.userphoneapi.dto;

/**
 * Exercício 2 – DTO de entrada para criação de um telefone
 *
 * Separei esse record do PhoneResponseDTO porque entrada e saída
 * têm responsabilidades diferentes. Aqui não faz sentido ter o id
 * (quem cria não manda o id, o banco gera) e também não expõe
 * nenhum vínculo com a entidade JPA.
 *
 * Seguindo a nomenclatura recomendada:
 *   - "Create" indica que é para criação
 *   - "Request" deixa claro que é dado de ENTRADA
 *   - "DTO" finaliza o nome padronizado
 */
public record CreatePhoneRequestDTO(
        String number,
        String type
) {}
