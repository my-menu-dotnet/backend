ALTER TABLE category
    DROP CONSTRAINT fk206gdcpy21rjiey7bkynoltqv;

ALTER TABLE company_allowed_field
    DROP CONSTRAINT fk7j0mj2lf86o0ymcq6s2ehb5f5;

ALTER TABLE app_user_companies
    DROP CONSTRAINT fk9gkay0ict9sybrxicfexatqc1;

ALTER TABLE app_user_companies
    DROP CONSTRAINT fk9h1ticmyq1vtg4bvfkq0gbw6x;

ALTER TABLE company_categories
    DROP CONSTRAINT fkiuy0osl58bryy7dw1ke7bm0f6;

ALTER TABLE company_categories
    DROP CONSTRAINT fkwyhcpoct220pa1f0s7im8voi;

ALTER TABLE company_allowed_field
    DROP CONSTRAINT fkxgpf3r3h4r5umeo9ab14wcbb;

ALTER TABLE category
    ADD "order" INTEGER DEFAULT 0;

DROP TABLE app_user_companies CASCADE;

DROP TABLE company_allowed_field CASCADE;

DROP TABLE company_categories CASCADE;

ALTER TABLE category
    DROP COLUMN description;

ALTER TABLE category
    DROP COLUMN image_id;

DROP SEQUENCE analytic_company_access_seq CASCADE;

DROP SEQUENCE email_code_seq CASCADE;