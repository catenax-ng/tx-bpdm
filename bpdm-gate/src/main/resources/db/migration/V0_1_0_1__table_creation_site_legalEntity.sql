CREATE TABLE identifier_status
  (
     id            INT8 NOT NULL,
     created_at    TIMESTAMP WITH time zone NOT NULL,
     updated_at    TIMESTAMP WITH time zone NOT NULL,
     uuid          UUID NOT NULL,
     "name"        VARCHAR(255) NOT NULL,
     technical_key VARCHAR(255) NOT NULL,
     CONSTRAINT pk_identifier_status PRIMARY KEY (id),
     CONSTRAINT uc_identifier_status_uuid UNIQUE (uuid),
     CONSTRAINT uc_identifier_status_technical_key UNIQUE (technical_key)
  );


CREATE TABLE identifier_types
  (
     id            INT8 NOT NULL,
     created_at    TIMESTAMP WITH time zone NOT NULL,
     updated_at    TIMESTAMP WITH time zone NOT NULL,
     uuid          UUID NOT NULL,
     "name"        VARCHAR(255) NOT NULL,
     technical_key VARCHAR(255) NOT NULL,
     url           VARCHAR(255) NULL,
     CONSTRAINT pk_identifier_types PRIMARY KEY (id),
     CONSTRAINT uc_identifier_types_uuid UNIQUE (uuid)
  );


CREATE TABLE issuing_bodies
  (
     id            INT8 NOT NULL,
     created_at    TIMESTAMP WITH time zone NOT NULL,
     updated_at    TIMESTAMP WITH time zone NOT NULL,
     uuid          UUID NOT NULL,
     "name"        VARCHAR(255) NOT NULL,
     technical_key VARCHAR(255) NOT NULL,
     url           VARCHAR(255) NULL,
     CONSTRAINT pk_issuing_bodies PRIMARY KEY (id),
     CONSTRAINT uc_issuing_bodies_uuid UNIQUE (uuid)
  );


CREATE TABLE legal_form_categories
  (
     id         INT8 NOT NULL,
     created_at TIMESTAMP WITH time zone NOT NULL,
     updated_at TIMESTAMP WITH time zone NOT NULL,
     uuid       UUID NOT NULL,
     "name"     VARCHAR(255) NOT NULL,
     url        VARCHAR(255) NULL,
     CONSTRAINT pk_legal_form_categories PRIMARY KEY (id),
     CONSTRAINT uc_legal_form_categories_uuid UNIQUE (uuid)
  );


CREATE TABLE legal_forms
  (
     id            INT8 NOT NULL,
     created_at    TIMESTAMP WITH time zone NOT NULL,
     updated_at    TIMESTAMP WITH time zone NOT NULL,
     uuid          UUID NOT NULL,
     "language"    INT2 NOT NULL,
     abbreviation  VARCHAR(255) NULL,
     "name"        VARCHAR(255) NULL,
     technical_key VARCHAR(255) NOT NULL,
     url           VARCHAR(255) NULL,
     CONSTRAINT pk_legal_forms PRIMARY KEY (id),
     CONSTRAINT uc_legal_forms_uuid UNIQUE (uuid)
  );


CREATE TABLE roles
  (
     id            INT8 NOT NULL,
     created_at    TIMESTAMP WITH time zone NOT NULL,
     updated_at    TIMESTAMP WITH time zone NOT NULL,
     uuid          UUID NOT NULL,
     "name"        VARCHAR(255) NOT NULL,
     technical_key VARCHAR(255) NOT NULL,
     CONSTRAINT pk_roles PRIMARY KEY (id),
     CONSTRAINT uc_roles_uuid UNIQUE (uuid),
     CONSTRAINT uc_roles_technical_key UNIQUE (technical_key)
  );









CREATE TABLE legal_entities
  (
     id               INT8 NOT NULL,
     created_at       TIMESTAMP WITH time zone NOT NULL,
     updated_at       TIMESTAMP WITH time zone NOT NULL,
     uuid             UUID NOT NULL,
     bpn              VARCHAR(255) NOT NULL,
     currentness      TIMESTAMP WITH time zone NOT NULL,
     external_id      VARCHAR(255) NOT NULL,
     legal_address_id INT8 NOT NULL,
     legal_form_id    INT8 NULL,
     CONSTRAINT pk_legal_entities PRIMARY KEY (id),
     CONSTRAINT uc_legal_entities UNIQUE (external_id),
     CONSTRAINT uc_legal_entities_uuid UNIQUE (uuid),
     CONSTRAINT uc_legal_entities_bpn UNIQUE (bpn),
     CONSTRAINT fk_legal_entities_on_address FOREIGN KEY (legal_address_id)
     REFERENCES addresses(id),
     CONSTRAINT fk_legal_entities_on_legal_forms FOREIGN KEY (legal_form_id)
     REFERENCES legal_forms(id)
  );
CREATE INDEX idx_m9ojfna20safop6xndvj1510n ON legal_entities USING btree (legal_form_id);

CREATE TABLE legal_entity_roles
  (
     legal_entity_id INT8 NOT NULL,
     role_id         INT8 NOT NULL,
     CONSTRAINT pk_legal_entity_roles PRIMARY KEY (legal_entity_id, role_id),
     CONSTRAINT fk_legal_entity_roles FOREIGN KEY (role_id) REFERENCES
     roles(id),
     CONSTRAINT fk_legal_entity_roles_on_legal_entities FOREIGN KEY (
     legal_entity_id) REFERENCES legal_entities(id)
  );
CREATE INDEX idx_d5n7954r4y9rj77ru0vvxv35x ON legal_entity_roles USING btree (legal_entity_id);


CREATE TABLE legal_entity_types
  (
     legal_entity_id INT8 NOT NULL,
     "type"          VARCHAR(255) NOT NULL,
     CONSTRAINT pk_legal_entity_types PRIMARY KEY (legal_entity_id, type),
     CONSTRAINT fk_legal_entity_types_on_legal_entities FOREIGN KEY (
     legal_entity_id) REFERENCES legal_entities(id)
  );
CREATE INDEX idx_l90kj28xlq30e0gdq06rwsl06 ON legal_entity_types USING btree (legal_entity_id);


CREATE TABLE legal_forms_legal_categories
  (
     form_id     INT8 NOT NULL,
     category_id INT8 NOT NULL,
     CONSTRAINT pklegal_forms_legal_categories PRIMARY KEY (form_id, category_id
     ),
     CONSTRAINT fk_legal_forms_legal_categories_on_legal_forms FOREIGN KEY (
     form_id) REFERENCES legal_forms(id),
     CONSTRAINT fk_legal_forms_legal_categories_on_legal_form_categories FOREIGN
     KEY (category_id) REFERENCES legal_form_categories(id)
  );
CREATE INDEX idx_2vkekrtd36sp15dukkpjskh2m ON legal_forms_legal_categories USING btree (form_id);
CREATE INDEX idx_4976357rtrnbtoc7dlqhw66c9 ON legal_forms_legal_categories USING btree (category_id);




-- Names Table
CREATE TABLE names
  (
     id              INT8 NOT NULL,
     created_at      TIMESTAMP WITH time zone NOT NULL,
     updated_at      TIMESTAMP WITH time zone NOT NULL,
     uuid            UUID NOT NULL,
     "language"      VARCHAR(255) NOT NULL,
     short_name      VARCHAR(255) NULL,
     "type"          VARCHAR(255) NOT NULL,
     value           VARCHAR(255) NOT NULL,
     legal_entity_id INT8 NOT NULL,
     CONSTRAINT pk_names PRIMARY KEY (id)
  );

CREATE INDEX idx2df02bfap6jciq17c370039qt ON names USING btree (legal_entity_id);

ALTER TABLE names
    ADD CONSTRAINT uc_names_uuid UNIQUE (uuid);

ALTER TABLE names
    ADD CONSTRAINT FK_NAMES_ON_PARTNER FOREIGN KEY (legal_entity_id) REFERENCES legal_entities (id);







-- Relations Table
CREATE TABLE relations
  (
     id            INT8 NOT NULL,
     created_at    TIMESTAMP WITH time zone NOT NULL,
     updated_at    TIMESTAMP WITH time zone NOT NULL,
     uuid          UUID NOT NULL,
     ended_at      TIMESTAMP(6) NULL,
     "class"       VARCHAR(255) NOT NULL,
     started_at    TIMESTAMP(6) NULL,
     "type"        VARCHAR(255) NOT NULL,
     end_node_id   INT8 NOT NULL,
     start_node_id INT8 NOT NULL,
     CONSTRAINT pk_relations PRIMARY KEY (id)
  );
CREATE INDEX idx9kgukliy4u1s9woyd0tk1nmm3 ON relations USING btree (start_node_id);
CREATE INDEX idxqhf4eyra7qpla4kh89miwsnv1 ON relations USING btree (end_node_id);

ALTER TABLE relations
    ADD CONSTRAINT uc_relations_uuid UNIQUE (uuid);

ALTER TABLE relations
    ADD CONSTRAINT FK_RELATIONS_ON_END_NODE FOREIGN KEY (end_node_id) REFERENCES legal_entities (id);

ALTER TABLE relations
    ADD CONSTRAINT FK_RELATIONS_ON_START_NODE FOREIGN KEY (start_node_id) REFERENCES legal_entities (id);

-- Sites Table
CREATE TABLE sites
  (
     id                       INT8 NOT NULL,
     created_at               TIMESTAMP WITH time zone NOT NULL,
     updated_at               TIMESTAMP WITH time zone NOT NULL,
     uuid                     UUID NOT NULL,
     bpn                      VARCHAR(255) NOT NULL,
     external_id              VARCHAR(255) NOT NULL,
     legal_entity_external_id VARCHAR(255) NULL,
     "name"                   VARCHAR(255) NOT NULL,
--      legal_entity_id          INT8 NOT NULL,
     main_address_id          INT8 NOT NULL,
     CONSTRAINT pk_site PRIMARY KEY (id)
  );

ALTER TABLE sites
    ADD CONSTRAINT uc_sites_uuid UNIQUE (uuid);

ALTER TABLE sites
    ADD CONSTRAINT uc_sites_external_id UNIQUE (external_id);

ALTER TABLE sites
    ADD CONSTRAINT uc_sites_bpn UNIQUE (bpn);

ALTER TABLE sites
    ADD CONSTRAINT uc_sites_legal_entity_external_id UNIQUE (legal_entity_external_id);

ALTER TABLE sites
    ADD CONSTRAINT FK_SITES_ON_MAIN_ADDRESS FOREIGN KEY (main_address_id) REFERENCES addresses (id);

-- ALTER TABLE sites
--     ADD CONSTRAINT FK_SITES_ON_LEGAL_ENTITY FOREIGN KEY (legal_entity_id) REFERENCES legal_entities (id);



-- Address Partners Table
CREATE TABLE address_partners
  (
     id              INT8 NOT NULL,
     created_at      TIMESTAMP WITH time zone NOT NULL,
     updated_at      TIMESTAMP WITH time zone NOT NULL,
     uuid            UUID NOT NULL,
     bpn             VARCHAR(255) NULL,
     address_id      INT8 NULL,
     legal_entity_id INT8 NULL,
     site_id         INT8 NULL,
     CONSTRAINT pk_address_partners PRIMARY KEY (id),
     CONSTRAINT uk_a1ie8abutkcme24w8jg8n8gse UNIQUE (bpn)
  );
CREATE INDEX idxekeyc5bebiebgxgyfod241rkg ON address_partners USING btree (site_id);
CREATE INDEX idxitgfpbcg97xng4hpyvx72oj0y ON address_partners USING btree (legal_entity_id);

ALTER TABLE address_partners
    ADD CONSTRAINT uc_address_partners_uuid UNIQUE (uuid);

ALTER TABLE address_partners
    ADD CONSTRAINT FK_ADDRESS_PARTNERS_ON_SITE FOREIGN KEY (site_id) REFERENCES sites (id);

ALTER TABLE address_partners
    ADD CONSTRAINT FK_ADDRESS_PARTNERS_ON_LEGAL_ENTITY FOREIGN KEY (legal_entity_id) REFERENCES legal_entities (id);

ALTER TABLE address_partners
    ADD CONSTRAINT FK_ADDRESS_PARTNERS_ON_ADDRESS FOREIGN KEY (address_id) REFERENCES addresses (id);

-- Bank Accounts Table
CREATE TABLE bank_accounts
  (
     id                               INT8 NOT NULL,
     created_at                       TIMESTAMP WITH time zone NOT NULL,
     updated_at                       TIMESTAMP WITH time zone NOT NULL,
     uuid                             UUID NOT NULL,
     currency                         VARCHAR(255) NOT NULL,
     international_account_identifier VARCHAR(255) NULL,
     international_bank_identifier    VARCHAR(255) NULL,
     national_account_identifier      VARCHAR(255) NULL,
     national_bank_identifier         VARCHAR(255) NULL,
     legal_entity_id                  INT8 NOT NULL,
     CONSTRAINT pk_bank_accounts PRIMARY KEY (id)
  );

CREATE INDEX idx8yh3qu1wpem5urty6p6cepsaq ON bank_accounts USING btree (legal_entity_id);

ALTER TABLE bank_accounts
    ADD CONSTRAINT uc_bank_accounts_uuid UNIQUE (uuid);

ALTER TABLE bank_accounts
    ADD CONSTRAINT FK_BANK_ACCOUNTS_ON_LEGAL_ENTITY FOREIGN KEY (legal_entity_id) REFERENCES legal_entities (id);

-- Business Stati Table
CREATE TABLE business_stati
  (
     id              INT8 NOT NULL,
     created_at      TIMESTAMP WITH time zone NOT NULL,
     updated_at      TIMESTAMP WITH time zone NOT NULL,
     uuid            UUID NOT NULL,
     denotation      VARCHAR(255) NULL,
     "type"          INT2 NOT NULL,
     valid_from      TIMESTAMP(6) NULL,
     valid_to        TIMESTAMP(6) NULL,
     legal_entity_id INT8 NOT NULL,
     CONSTRAINT pk_business_stati PRIMARY KEY (id)
  );
CREATE INDEX idxre5j6xugjkooe2sw4xoxpgh2u ON business_stati USING btree (legal_entity_id);

ALTER TABLE business_stati
    ADD CONSTRAINT uc_business_stati_uuid UNIQUE (uuid);

ALTER TABLE business_stati
    ADD CONSTRAINT FK_BUSINESS_STATI_ON_LEGAL_ENTITY FOREIGN KEY (legal_entity_id) REFERENCES legal_entities (id);

-- Classifications Table
CREATE TABLE classifications
  (
     id              INT8 NOT NULL,
     created_at      TIMESTAMP WITH time zone NOT NULL,
     updated_at      TIMESTAMP WITH time zone NOT NULL,
     uuid            UUID NOT NULL,
     code            VARCHAR(255) NULL,
     "type"          VARCHAR(255) NULL,
     value           VARCHAR(255) NULL,
     legal_entity_id INT8 NOT NULL,
     CONSTRAINT pk_classifications PRIMARY KEY (id)
  );
CREATE INDEX idx6g01nxco0d10y4an0kt91rwp6 ON classifications USING btree (legal_entity_id);

ALTER TABLE classifications
    ADD CONSTRAINT uc_classifications_uuid UNIQUE (uuid);

ALTER TABLE classifications
    ADD CONSTRAINT FK_CLASSIFICATIONS_ON_LEGAL_ENTITY FOREIGN KEY (legal_entity_id) REFERENCES legal_entities (id);

-- Identifiers Table
CREATE TABLE identifiers
  (
     id              INT8 NOT NULL,
     created_at      TIMESTAMP WITH time zone NOT NULL,
     updated_at      TIMESTAMP WITH time zone NOT NULL,
     uuid            UUID NOT NULL,
     value           VARCHAR(255) NOT NULL,
     issuing_body_id INT8 NULL,
     legal_entity_id INT8 NOT NULL,
     status          INT8 NULL,
     type_id         INT8 NOT NULL,
     CONSTRAINT pk_identifiers PRIMARY KEY (id)
  );
CREATE INDEX idx165b3dmxtvf0gv4ddvr1qke3b ON identifiers USING btree (status);
CREATE INDEX idxcor8gn1asqjwtiifvckomktgl ON identifiers USING btree (issuing_body_id);
CREATE INDEX idxrqhrpfkxnhiplfx4omxyvvbyv ON identifiers USING btree (type_id);
CREATE INDEX idxy52013s3vtpghrehn526vi82 ON identifiers USING btree (legal_entity_id);

ALTER TABLE identifiers
    ADD CONSTRAINT uc_identifiers_uuid UNIQUE (uuid);

ALTER TABLE identifiers
    ADD CONSTRAINT FK_IDENTIFIERS_ON_ISSUING_BODY FOREIGN KEY (issuing_body_id) REFERENCES issuing_bodies (id);

ALTER TABLE identifiers
    ADD CONSTRAINT FK_IDENTIFIERS_ON_LEGAL_ENTITY FOREIGN KEY (legal_entity_id) REFERENCES legal_entities (id);

ALTER TABLE identifiers
    ADD CONSTRAINT FK_IDENTIFIERS_ON_STATUS FOREIGN KEY (status) REFERENCES identifier_status (id);

ALTER TABLE identifiers
    ADD CONSTRAINT FK_IDENTIFIERS_ON_TYPE FOREIGN KEY (type_id) REFERENCES identifier_types (id);

-- Bank Account Trust Scores Table
CREATE TABLE bank_account_trust_scores
  (
     account_id INT8 NOT NULL,
     score      FLOAT4 NOT NULL,
     CONSTRAINT pk_bank_account_trust_scores PRIMARY KEY (account_id, score)
  );
CREATE INDEX idxefw11vgnqp4gjeogmmuja0poc ON bank_account_trust_scores USING btree (account_id);

ALTER TABLE bank_account_trust_scores
    ADD CONSTRAINT fk_bank_account_trust_scores_on_bank_account FOREIGN KEY (account_id) REFERENCES bank_accounts (id);