PGDMP  7            
        |           everest_model    16.2 (Postgres.app)    16.1 �               0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false                       1262    16391    everest_model    DATABASE     y   CREATE DATABASE everest_model WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.UTF-8';
    DROP DATABASE everest_model;
                everest_admin    false            �            1259    17152    area    TABLE     |   CREATE TABLE public.area (
    id bigint NOT NULL,
    version bigint NOT NULL,
    name character varying(100) NOT NULL
);
    DROP TABLE public.area;
       public         heap    everest_admin    false            �            1259    17157    automated_test    TABLE       CREATE TABLE public.automated_test (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    name character varying(255),
    full_name character varying(500) NOT NULL,
    project_id bigint NOT NULL
);
 "   DROP TABLE public.automated_test;
       public         heap    everest_admin    false            �            1259    17164    bug    TABLE       CREATE TABLE public.bug (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    platform character varying(7),
    notes character varying(500),
    person_id bigint NOT NULL,
    name character varying(255) NOT NULL,
    actual character varying(500),
    status character varying(6) NOT NULL,
    expected character varying(500),
    description character varying(1000),
    project_id bigint NOT NULL,
    area_id bigint
);
    DROP TABLE public.bug;
       public         heap    everest_admin    false            �            1259    17171    bug_environment    TABLE     �   CREATE TABLE public.bug_environment (
    bug_environments_id bigint,
    environment_id bigint,
    environments_idx integer
);
 #   DROP TABLE public.bug_environment;
       public         heap    everest_admin    false            �            1259    17174    bug_step    TABLE     e   CREATE TABLE public.bug_step (
    bug_steps_id bigint,
    step_id bigint,
    steps_idx integer
);
    DROP TABLE public.bug_step;
       public         heap    everest_admin    false            �            1259    17177    environment    TABLE     �   CREATE TABLE public.environment (
    id bigint NOT NULL,
    version bigint NOT NULL,
    name character varying(100) NOT NULL
);
    DROP TABLE public.environment;
       public         heap    everest_admin    false            �            1259    17151    hibernate_sequence    SEQUENCE     {   CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.hibernate_sequence;
       public          everest_admin    false            �            1259    17182    iteration_step    TABLE     �   CREATE TABLE public.iteration_step (
    id bigint NOT NULL,
    version bigint NOT NULL,
    act character varying(500),
    data character varying(500),
    result character varying(500)
);
 "   DROP TABLE public.iteration_step;
       public         heap    everest_admin    false            �            1259    17189    link    TABLE     �   CREATE TABLE public.link (
    id bigint NOT NULL,
    version bigint NOT NULL,
    owner_id bigint NOT NULL,
    project_id bigint NOT NULL,
    linked_id bigint NOT NULL,
    relation character varying(13) NOT NULL
);
    DROP TABLE public.link;
       public         heap    everest_admin    false            �            1259    17194    person    TABLE     =  CREATE TABLE public.person (
    id bigint NOT NULL,
    version bigint NOT NULL,
    password_expired boolean NOT NULL,
    account_locked boolean NOT NULL,
    password character varying(256) NOT NULL,
    account_expired boolean NOT NULL,
    enabled boolean NOT NULL,
    email character varying(255) NOT NULL
);
    DROP TABLE public.person;
       public         heap    everest_admin    false            �            1259    17201    person_role    TABLE     `   CREATE TABLE public.person_role (
    person_id bigint NOT NULL,
    role_id bigint NOT NULL
);
    DROP TABLE public.person_role;
       public         heap    everest_admin    false            �            1259    17206    project    TABLE     �   CREATE TABLE public.project (
    id bigint NOT NULL,
    version bigint NOT NULL,
    code character varying(5) NOT NULL,
    name character varying(100) NOT NULL
);
    DROP TABLE public.project;
       public         heap    everest_admin    false            �            1259    17211    project_area    TABLE     _   CREATE TABLE public.project_area (
    project_areas_id bigint NOT NULL,
    area_id bigint
);
     DROP TABLE public.project_area;
       public         heap    everest_admin    false            �            1259    17214    project_environment    TABLE     t   CREATE TABLE public.project_environment (
    project_environments_id bigint NOT NULL,
    environment_id bigint
);
 '   DROP TABLE public.project_environment;
       public         heap    everest_admin    false            �            1259    17217    registration_code    TABLE     �   CREATE TABLE public.registration_code (
    id bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    username character varying(255) NOT NULL,
    token character varying(255) NOT NULL
);
 %   DROP TABLE public.registration_code;
       public         heap    everest_admin    false            �            1259    17224    release_plan    TABLE     �  CREATE TABLE public.release_plan (
    id bigint NOT NULL,
    version bigint NOT NULL,
    person_id bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    name character varying(500) NOT NULL,
    release_date timestamp without time zone,
    status character varying(11) NOT NULL,
    planned_date timestamp without time zone,
    project_id bigint NOT NULL
);
     DROP TABLE public.release_plan;
       public         heap    everest_admin    false            �            1259    17231    role    TABLE     �   CREATE TABLE public.role (
    id bigint NOT NULL,
    version bigint NOT NULL,
    authority character varying(255) NOT NULL
);
    DROP TABLE public.role;
       public         heap    everest_admin    false            �            1259    17236    scenario    TABLE     �  CREATE TABLE public.scenario (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    platform character varying(7),
    person_id bigint NOT NULL,
    execution_method character varying(9),
    name character varying(255) NOT NULL,
    type character varying(3),
    gherkin character varying(2500),
    description character varying(1000),
    project_id bigint NOT NULL,
    area_id bigint
);
    DROP TABLE public.scenario;
       public         heap    everest_admin    false            �            1259    17243    scenario_environment    TABLE     �   CREATE TABLE public.scenario_environment (
    scenario_environments_id bigint,
    environment_id bigint,
    environments_idx integer
);
 (   DROP TABLE public.scenario_environment;
       public         heap    everest_admin    false            �            1259    17246    step    TABLE     b  CREATE TABLE public.step (
    id bigint NOT NULL,
    version bigint NOT NULL,
    act character varying(500),
    template_id bigint,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    is_builder_step boolean NOT NULL,
    data character varying(500),
    result character varying(500)
);
    DROP TABLE public.step;
       public         heap    everest_admin    false            �            1259    17253    step_template    TABLE     u  CREATE TABLE public.step_template (
    id bigint NOT NULL,
    version bigint NOT NULL,
    act character varying(500),
    person_id bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    name character varying(255) NOT NULL,
    result character varying(500),
    project_id bigint NOT NULL
);
 !   DROP TABLE public.step_template;
       public         heap    everest_admin    false            �            1259    17260 	   test_case    TABLE     �  CREATE TABLE public.test_case (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    platform character varying(7),
    person_id bigint NOT NULL,
    execution_method character varying(9),
    name character varying(255) NOT NULL,
    type character varying(3),
    description character varying(1000),
    project_id bigint NOT NULL,
    verify character varying(500),
    area_id bigint
);
    DROP TABLE public.test_case;
       public         heap    everest_admin    false            �            1259    17267    test_case_environment    TABLE     x   CREATE TABLE public.test_case_environment (
    test_case_environments_id bigint NOT NULL,
    environment_id bigint
);
 )   DROP TABLE public.test_case_environment;
       public         heap    everest_admin    false            �            1259    17270    test_case_step    TABLE     q   CREATE TABLE public.test_case_step (
    test_case_steps_id bigint,
    step_id bigint,
    steps_idx integer
);
 "   DROP TABLE public.test_case_step;
       public         heap    everest_admin    false            �            1259    17273    test_case_test_groups    TABLE     �   CREATE TABLE public.test_case_test_groups (
    test_group_id bigint NOT NULL,
    test_case_id bigint NOT NULL,
    test_groups_idx integer
);
 )   DROP TABLE public.test_case_test_groups;
       public         heap    everest_admin    false            �            1259    17276 
   test_cycle    TABLE       CREATE TABLE public.test_cycle (
    id bigint NOT NULL,
    version bigint NOT NULL,
    environ_id bigint,
    date_created timestamp without time zone NOT NULL,
    name character varying(500) NOT NULL,
    platform character varying(7),
    release_plan_id bigint NOT NULL
);
    DROP TABLE public.test_cycle;
       public         heap    everest_admin    false            �            1259    17283    test_cycle_test_case_ids    TABLE     j   CREATE TABLE public.test_cycle_test_case_ids (
    test_cycle_id bigint,
    test_case_ids_long bigint
);
 ,   DROP TABLE public.test_cycle_test_case_ids;
       public         heap    everest_admin    false            �            1259    17286 
   test_group    TABLE     �   CREATE TABLE public.test_group (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    name character varying(255) NOT NULL,
    project_id bigint NOT NULL
);
    DROP TABLE public.test_group;
       public         heap    everest_admin    false            �            1259    17291    test_iteration    TABLE     �  CREATE TABLE public.test_iteration (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    notes character varying(1000),
    result character varying(6) NOT NULL,
    test_cycle_id bigint NOT NULL,
    person_id bigint,
    date_executed timestamp without time zone,
    name character varying(255) NOT NULL,
    test_case_id bigint NOT NULL,
    verify character varying(500)
);
 "   DROP TABLE public.test_iteration;
       public         heap    everest_admin    false            �            1259    17298    test_iteration_iteration_step    TABLE     �   CREATE TABLE public.test_iteration_iteration_step (
    test_iteration_steps_id bigint NOT NULL,
    iteration_step_id bigint,
    steps_idx integer
);
 1   DROP TABLE public.test_iteration_iteration_step;
       public         heap    everest_admin    false            �            1259    17301    test_result    TABLE       CREATE TABLE public.test_result (
    id bigint NOT NULL,
    version bigint NOT NULL,
    failure_cause character varying(500),
    date_created timestamp without time zone NOT NULL,
    automated_test_id bigint NOT NULL,
    result character varying(7) NOT NULL
);
    DROP TABLE public.test_result;
       public         heap    everest_admin    false            �            1259    17308    test_run    TABLE     �   CREATE TABLE public.test_run (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    name character varying(255) NOT NULL,
    project_id bigint NOT NULL
);
    DROP TABLE public.test_run;
       public         heap    everest_admin    false            �            1259    17313    test_run_test_result    TABLE     v   CREATE TABLE public.test_run_test_result (
    test_run_test_results_id bigint NOT NULL,
    test_result_id bigint
);
 (   DROP TABLE public.test_run_test_result;
       public         heap    everest_admin    false            �          0    17152    area 
   TABLE DATA           1   COPY public.area (id, version, name) FROM stdin;
    public          everest_admin    false    216   A�       �          0    17157    automated_test 
   TABLE DATA           `   COPY public.automated_test (id, version, date_created, name, full_name, project_id) FROM stdin;
    public          everest_admin    false    217   ^�       �          0    17164    bug 
   TABLE DATA           �   COPY public.bug (id, version, date_created, last_updated, platform, notes, person_id, name, actual, status, expected, description, project_id, area_id) FROM stdin;
    public          everest_admin    false    218   {�       �          0    17171    bug_environment 
   TABLE DATA           `   COPY public.bug_environment (bug_environments_id, environment_id, environments_idx) FROM stdin;
    public          everest_admin    false    219   ��       �          0    17174    bug_step 
   TABLE DATA           D   COPY public.bug_step (bug_steps_id, step_id, steps_idx) FROM stdin;
    public          everest_admin    false    220   ��       �          0    17177    environment 
   TABLE DATA           8   COPY public.environment (id, version, name) FROM stdin;
    public          everest_admin    false    221   Ҿ       �          0    17182    iteration_step 
   TABLE DATA           H   COPY public.iteration_step (id, version, act, data, result) FROM stdin;
    public          everest_admin    false    222   �       �          0    17189    link 
   TABLE DATA           V   COPY public.link (id, version, owner_id, project_id, linked_id, relation) FROM stdin;
    public          everest_admin    false    223   �       �          0    17194    person 
   TABLE DATA           z   COPY public.person (id, version, password_expired, account_locked, password, account_expired, enabled, email) FROM stdin;
    public          everest_admin    false    224   )�       �          0    17201    person_role 
   TABLE DATA           9   COPY public.person_role (person_id, role_id) FROM stdin;
    public          everest_admin    false    225   ��       �          0    17206    project 
   TABLE DATA           :   COPY public.project (id, version, code, name) FROM stdin;
    public          everest_admin    false    226   ȿ       �          0    17211    project_area 
   TABLE DATA           A   COPY public.project_area (project_areas_id, area_id) FROM stdin;
    public          everest_admin    false    227   �       �          0    17214    project_environment 
   TABLE DATA           V   COPY public.project_environment (project_environments_id, environment_id) FROM stdin;
    public          everest_admin    false    228   �                  0    17217    registration_code 
   TABLE DATA           N   COPY public.registration_code (id, date_created, username, token) FROM stdin;
    public          everest_admin    false    229   �                 0    17224    release_plan 
   TABLE DATA           �   COPY public.release_plan (id, version, person_id, date_created, last_updated, name, release_date, status, planned_date, project_id) FROM stdin;
    public          everest_admin    false    230   <�                 0    17231    role 
   TABLE DATA           6   COPY public.role (id, version, authority) FROM stdin;
    public          everest_admin    false    231   Y�                 0    17236    scenario 
   TABLE DATA           �   COPY public.scenario (id, version, date_created, last_updated, platform, person_id, execution_method, name, type, gherkin, description, project_id, area_id) FROM stdin;
    public          everest_admin    false    232   ��                 0    17243    scenario_environment 
   TABLE DATA           j   COPY public.scenario_environment (scenario_environments_id, environment_id, environments_idx) FROM stdin;
    public          everest_admin    false    233   ��                 0    17246    step 
   TABLE DATA           x   COPY public.step (id, version, act, template_id, date_created, last_updated, is_builder_step, data, result) FROM stdin;
    public          everest_admin    false    234   ��                 0    17253    step_template 
   TABLE DATA           z   COPY public.step_template (id, version, act, person_id, date_created, last_updated, name, result, project_id) FROM stdin;
    public          everest_admin    false    235   �                 0    17260 	   test_case 
   TABLE DATA           �   COPY public.test_case (id, version, date_created, last_updated, platform, person_id, execution_method, name, type, description, project_id, verify, area_id) FROM stdin;
    public          everest_admin    false    236   �                 0    17267    test_case_environment 
   TABLE DATA           Z   COPY public.test_case_environment (test_case_environments_id, environment_id) FROM stdin;
    public          everest_admin    false    237   ;�       	          0    17270    test_case_step 
   TABLE DATA           P   COPY public.test_case_step (test_case_steps_id, step_id, steps_idx) FROM stdin;
    public          everest_admin    false    238   X�       
          0    17273    test_case_test_groups 
   TABLE DATA           ]   COPY public.test_case_test_groups (test_group_id, test_case_id, test_groups_idx) FROM stdin;
    public          everest_admin    false    239   u�                 0    17276 
   test_cycle 
   TABLE DATA           l   COPY public.test_cycle (id, version, environ_id, date_created, name, platform, release_plan_id) FROM stdin;
    public          everest_admin    false    240   ��                 0    17283    test_cycle_test_case_ids 
   TABLE DATA           U   COPY public.test_cycle_test_case_ids (test_cycle_id, test_case_ids_long) FROM stdin;
    public          everest_admin    false    241   ��                 0    17286 
   test_group 
   TABLE DATA           Q   COPY public.test_group (id, version, date_created, name, project_id) FROM stdin;
    public          everest_admin    false    242   ��                 0    17291    test_iteration 
   TABLE DATA           �   COPY public.test_iteration (id, version, date_created, last_updated, notes, result, test_cycle_id, person_id, date_executed, name, test_case_id, verify) FROM stdin;
    public          everest_admin    false    243   ��                 0    17298    test_iteration_iteration_step 
   TABLE DATA           n   COPY public.test_iteration_iteration_step (test_iteration_steps_id, iteration_step_id, steps_idx) FROM stdin;
    public          everest_admin    false    244   �                 0    17301    test_result 
   TABLE DATA           j   COPY public.test_result (id, version, failure_cause, date_created, automated_test_id, result) FROM stdin;
    public          everest_admin    false    245   #�                 0    17308    test_run 
   TABLE DATA           O   COPY public.test_run (id, version, date_created, name, project_id) FROM stdin;
    public          everest_admin    false    246   @�                 0    17313    test_run_test_result 
   TABLE DATA           X   COPY public.test_run_test_result (test_run_test_results_id, test_result_id) FROM stdin;
    public          everest_admin    false    247   ]�                  0    0    hibernate_sequence    SEQUENCE SET     @   SELECT pg_catalog.setval('public.hibernate_sequence', 5, true);
          public          everest_admin    false    215                       2606    17156    area area_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.area
    ADD CONSTRAINT area_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.area DROP CONSTRAINT area_pkey;
       public            everest_admin    false    216                       2606    17163 "   automated_test automated_test_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.automated_test
    ADD CONSTRAINT automated_test_pkey PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.automated_test DROP CONSTRAINT automated_test_pkey;
       public            everest_admin    false    217                       2606    17170    bug bug_pkey 
   CONSTRAINT     J   ALTER TABLE ONLY public.bug
    ADD CONSTRAINT bug_pkey PRIMARY KEY (id);
 6   ALTER TABLE ONLY public.bug DROP CONSTRAINT bug_pkey;
       public            everest_admin    false    218                       2606    17181    environment environment_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.environment
    ADD CONSTRAINT environment_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.environment DROP CONSTRAINT environment_pkey;
       public            everest_admin    false    221                       2606    17188 "   iteration_step iteration_step_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.iteration_step
    ADD CONSTRAINT iteration_step_pkey PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.iteration_step DROP CONSTRAINT iteration_step_pkey;
       public            everest_admin    false    222                       2606    17193    link link_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.link
    ADD CONSTRAINT link_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.link DROP CONSTRAINT link_pkey;
       public            everest_admin    false    223                       2606    17200    person person_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.person
    ADD CONSTRAINT person_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.person DROP CONSTRAINT person_pkey;
       public            everest_admin    false    224                       2606    17205    person_role person_role_pkey 
   CONSTRAINT     j   ALTER TABLE ONLY public.person_role
    ADD CONSTRAINT person_role_pkey PRIMARY KEY (person_id, role_id);
 F   ALTER TABLE ONLY public.person_role DROP CONSTRAINT person_role_pkey;
       public            everest_admin    false    225    225                       2606    17210    project project_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.project
    ADD CONSTRAINT project_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.project DROP CONSTRAINT project_pkey;
       public            everest_admin    false    226                        2606    17223 (   registration_code registration_code_pkey 
   CONSTRAINT     f   ALTER TABLE ONLY public.registration_code
    ADD CONSTRAINT registration_code_pkey PRIMARY KEY (id);
 R   ALTER TABLE ONLY public.registration_code DROP CONSTRAINT registration_code_pkey;
       public            everest_admin    false    229            "           2606    17230    release_plan release_plan_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.release_plan
    ADD CONSTRAINT release_plan_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.release_plan DROP CONSTRAINT release_plan_pkey;
       public            everest_admin    false    230            $           2606    17235    role role_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.role DROP CONSTRAINT role_pkey;
       public            everest_admin    false    231            (           2606    17242    scenario scenario_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.scenario
    ADD CONSTRAINT scenario_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.scenario DROP CONSTRAINT scenario_pkey;
       public            everest_admin    false    232            *           2606    17252    step step_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.step
    ADD CONSTRAINT step_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.step DROP CONSTRAINT step_pkey;
       public            everest_admin    false    234            ,           2606    17259     step_template step_template_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.step_template
    ADD CONSTRAINT step_template_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY public.step_template DROP CONSTRAINT step_template_pkey;
       public            everest_admin    false    235            .           2606    17266    test_case test_case_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.test_case
    ADD CONSTRAINT test_case_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.test_case DROP CONSTRAINT test_case_pkey;
       public            everest_admin    false    236            0           2606    17282    test_cycle test_cycle_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.test_cycle
    ADD CONSTRAINT test_cycle_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.test_cycle DROP CONSTRAINT test_cycle_pkey;
       public            everest_admin    false    240            2           2606    17290    test_group test_group_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.test_group
    ADD CONSTRAINT test_group_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.test_group DROP CONSTRAINT test_group_pkey;
       public            everest_admin    false    242            4           2606    17297 "   test_iteration test_iteration_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.test_iteration
    ADD CONSTRAINT test_iteration_pkey PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.test_iteration DROP CONSTRAINT test_iteration_pkey;
       public            everest_admin    false    243            6           2606    17307    test_result test_result_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.test_result
    ADD CONSTRAINT test_result_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.test_result DROP CONSTRAINT test_result_pkey;
       public            everest_admin    false    245            8           2606    17312    test_run test_run_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.test_run
    ADD CONSTRAINT test_run_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.test_run DROP CONSTRAINT test_run_pkey;
       public            everest_admin    false    246            
           2606    17317 -   automated_test uk8a9d9878ace51399c8abd8b049d1 
   CONSTRAINT     y   ALTER TABLE ONLY public.automated_test
    ADD CONSTRAINT uk8a9d9878ace51399c8abd8b049d1 UNIQUE (project_id, full_name);
 W   ALTER TABLE ONLY public.automated_test DROP CONSTRAINT uk8a9d9878ace51399c8abd8b049d1;
       public            everest_admin    false    217    217                       2606    17323 $   project uk_3k75vvu7mevyvvb5may5lj8k7 
   CONSTRAINT     _   ALTER TABLE ONLY public.project
    ADD CONSTRAINT uk_3k75vvu7mevyvvb5may5lj8k7 UNIQUE (name);
 N   ALTER TABLE ONLY public.project DROP CONSTRAINT uk_3k75vvu7mevyvvb5may5lj8k7;
       public            everest_admin    false    226                       2606    17321 $   project uk_eh3nusutt0qy84a4yr9pfxkyg 
   CONSTRAINT     _   ALTER TABLE ONLY public.project
    ADD CONSTRAINT uk_eh3nusutt0qy84a4yr9pfxkyg UNIQUE (code);
 N   ALTER TABLE ONLY public.project DROP CONSTRAINT uk_eh3nusutt0qy84a4yr9pfxkyg;
       public            everest_admin    false    226                       2606    17319 #   person uk_fwmwi44u55bo4rvwsv0cln012 
   CONSTRAINT     _   ALTER TABLE ONLY public.person
    ADD CONSTRAINT uk_fwmwi44u55bo4rvwsv0cln012 UNIQUE (email);
 M   ALTER TABLE ONLY public.person DROP CONSTRAINT uk_fwmwi44u55bo4rvwsv0cln012;
       public            everest_admin    false    224            &           2606    17325 !   role uk_irsamgnera6angm0prq1kemt2 
   CONSTRAINT     a   ALTER TABLE ONLY public.role
    ADD CONSTRAINT uk_irsamgnera6angm0prq1kemt2 UNIQUE (authority);
 K   ALTER TABLE ONLY public.role DROP CONSTRAINT uk_irsamgnera6angm0prq1kemt2;
       public            everest_admin    false    231            M           2606    17426 )   step_template fk38krdos2kx2pc2i4nr81yivhx    FK CONSTRAINT     �   ALTER TABLE ONLY public.step_template
    ADD CONSTRAINT fk38krdos2kx2pc2i4nr81yivhx FOREIGN KEY (person_id) REFERENCES public.person(id);
 S   ALTER TABLE ONLY public.step_template DROP CONSTRAINT fk38krdos2kx2pc2i4nr81yivhx;
       public          everest_admin    false    235    224    3604            R           2606    17456 1   test_case_environment fk4ajnc7fbdc2iyj2k8ytmga4h0    FK CONSTRAINT     �   ALTER TABLE ONLY public.test_case_environment
    ADD CONSTRAINT fk4ajnc7fbdc2iyj2k8ytmga4h0 FOREIGN KEY (test_case_environments_id) REFERENCES public.test_case(id);
 [   ALTER TABLE ONLY public.test_case_environment DROP CONSTRAINT fk4ajnc7fbdc2iyj2k8ytmga4h0;
       public          everest_admin    false    3630    237    236            N           2606    17431 )   step_template fk50saotfxwhf1a26qoyfq8qyr9    FK CONSTRAINT     �   ALTER TABLE ONLY public.step_template
    ADD CONSTRAINT fk50saotfxwhf1a26qoyfq8qyr9 FOREIGN KEY (project_id) REFERENCES public.project(id);
 S   ALTER TABLE ONLY public.step_template DROP CONSTRAINT fk50saotfxwhf1a26qoyfq8qyr9;
       public          everest_admin    false    235    3610    226            O           2606    17446 $   test_case fk5a1omo3b7vn82yqckgn76cf5    FK CONSTRAINT     �   ALTER TABLE ONLY public.test_case
    ADD CONSTRAINT fk5a1omo3b7vn82yqckgn76cf5 FOREIGN KEY (area_id) REFERENCES public.area(id);
 N   ALTER TABLE ONLY public.test_case DROP CONSTRAINT fk5a1omo3b7vn82yqckgn76cf5;
       public          everest_admin    false    216    3590    236            H           2606    17411 $   scenario fk6hp1q8kdvfd0bs6vtr5k5ddui    FK CONSTRAINT     �   ALTER TABLE ONLY public.scenario
    ADD CONSTRAINT fk6hp1q8kdvfd0bs6vtr5k5ddui FOREIGN KEY (area_id) REFERENCES public.area(id);
 N   ALTER TABLE ONLY public.scenario DROP CONSTRAINT fk6hp1q8kdvfd0bs6vtr5k5ddui;
       public          everest_admin    false    216    3590    232            F           2606    17396 (   release_plan fk6y0f94nne0acxxhkpy2xy30uv    FK CONSTRAINT     �   ALTER TABLE ONLY public.release_plan
    ADD CONSTRAINT fk6y0f94nne0acxxhkpy2xy30uv FOREIGN KEY (project_id) REFERENCES public.project(id);
 R   ALTER TABLE ONLY public.release_plan DROP CONSTRAINT fk6y0f94nne0acxxhkpy2xy30uv;
       public          everest_admin    false    226    3610    230            a           2606    17526 0   test_run_test_result fk750hpvl66u37nppnchca3f8pr    FK CONSTRAINT     �   ALTER TABLE ONLY public.test_run_test_result
    ADD CONSTRAINT fk750hpvl66u37nppnchca3f8pr FOREIGN KEY (test_result_id) REFERENCES public.test_result(id);
 Z   ALTER TABLE ONLY public.test_run_test_result DROP CONSTRAINT fk750hpvl66u37nppnchca3f8pr;
       public          everest_admin    false    247    3638    245            :           2606    17341    bug fk8hnx6x9kyglxi85uqadpw4cgd    FK CONSTRAINT     }   ALTER TABLE ONLY public.bug
    ADD CONSTRAINT fk8hnx6x9kyglxi85uqadpw4cgd FOREIGN KEY (area_id) REFERENCES public.area(id);
 I   ALTER TABLE ONLY public.bug DROP CONSTRAINT fk8hnx6x9kyglxi85uqadpw4cgd;
       public          everest_admin    false    3590    218    216            9           2606    17326 )   automated_test fk8vvu4v9uwyssf5pqv5l4bemb    FK CONSTRAINT     �   ALTER TABLE ONLY public.automated_test
    ADD CONSTRAINT fk8vvu4v9uwyssf5pqv5l4bemb FOREIGN KEY (project_id) REFERENCES public.project(id);
 S   ALTER TABLE ONLY public.automated_test DROP CONSTRAINT fk8vvu4v9uwyssf5pqv5l4bemb;
       public          everest_admin    false    226    3610    217            T           2606    17461 *   test_case_step fk9lnmhnxmhiopdnc8vdfg7sh1x    FK CONSTRAINT     �   ALTER TABLE ONLY public.test_case_step
    ADD CONSTRAINT fk9lnmhnxmhiopdnc8vdfg7sh1x FOREIGN KEY (step_id) REFERENCES public.step(id);
 T   ALTER TABLE ONLY public.test_case_step DROP CONSTRAINT fk9lnmhnxmhiopdnc8vdfg7sh1x;
       public          everest_admin    false    234    238    3626            G           2606    17391 (   release_plan fk9pa4ds4owvxht9e0e73y6nanr    FK CONSTRAINT     �   ALTER TABLE ONLY public.release_plan
    ADD CONSTRAINT fk9pa4ds4owvxht9e0e73y6nanr FOREIGN KEY (person_id) REFERENCES public.person(id);
 R   ALTER TABLE ONLY public.release_plan DROP CONSTRAINT fk9pa4ds4owvxht9e0e73y6nanr;
       public          everest_admin    false    230    224    3604            ;           2606    17331    bug fkacv21dyrr01us0cwcf4erfil3    FK CONSTRAINT     �   ALTER TABLE ONLY public.bug
    ADD CONSTRAINT fkacv21dyrr01us0cwcf4erfil3 FOREIGN KEY (person_id) REFERENCES public.person(id);
 I   ALTER TABLE ONLY public.bug DROP CONSTRAINT fkacv21dyrr01us0cwcf4erfil3;
       public          everest_admin    false    218    3604    224            I           2606    17401 $   scenario fkadgolvhsnmgr7cr2wv6x415pt    FK CONSTRAINT     �   ALTER TABLE ONLY public.scenario
    ADD CONSTRAINT fkadgolvhsnmgr7cr2wv6x415pt FOREIGN KEY (person_id) REFERENCES public.person(id);
 N   ALTER TABLE ONLY public.scenario DROP CONSTRAINT fkadgolvhsnmgr7cr2wv6x415pt;
       public          everest_admin    false    232    3604    224            [           2606    17501 *   test_iteration fkahonylro8iob3xuqpjf3xknlr    FK CONSTRAINT     �   ALTER TABLE ONLY public.test_iteration
    ADD CONSTRAINT fkahonylro8iob3xuqpjf3xknlr FOREIGN KEY (person_id) REFERENCES public.person(id);
 T   ALTER TABLE ONLY public.test_iteration DROP CONSTRAINT fkahonylro8iob3xuqpjf3xknlr;
       public          everest_admin    false    243    224    3604            L           2606    17421     step fkalfmhcod1k10n8j35fbt26wlb    FK CONSTRAINT     �   ALTER TABLE ONLY public.step
    ADD CONSTRAINT fkalfmhcod1k10n8j35fbt26wlb FOREIGN KEY (template_id) REFERENCES public.step_template(id);
 J   ALTER TABLE ONLY public.step DROP CONSTRAINT fkalfmhcod1k10n8j35fbt26wlb;
       public          everest_admin    false    3628    234    235            B           2606    17371 (   project_area fkbf6m2u4tapd9cenngqjyqc774    FK CONSTRAINT     �   ALTER TABLE ONLY public.project_area
    ADD CONSTRAINT fkbf6m2u4tapd9cenngqjyqc774 FOREIGN KEY (area_id) REFERENCES public.area(id);
 R   ALTER TABLE ONLY public.project_area DROP CONSTRAINT fkbf6m2u4tapd9cenngqjyqc774;
       public          everest_admin    false    227    3590    216            P           2606    17436 %   test_case fkbxo28q05096mw3d323ty0bemf    FK CONSTRAINT     �   ALTER TABLE ONLY public.test_case
    ADD CONSTRAINT fkbxo28q05096mw3d323ty0bemf FOREIGN KEY (person_id) REFERENCES public.person(id);
 O   ALTER TABLE ONLY public.test_case DROP CONSTRAINT fkbxo28q05096mw3d323ty0bemf;
       public          everest_admin    false    236    224    3604            \           2606    17506 *   test_iteration fkcit7tt10rkk7ogyco9b3mpueb    FK CONSTRAINT     �   ALTER TABLE ONLY public.test_iteration
    ADD CONSTRAINT fkcit7tt10rkk7ogyco9b3mpueb FOREIGN KEY (test_case_id) REFERENCES public.test_case(id);
 T   ALTER TABLE ONLY public.test_iteration DROP CONSTRAINT fkcit7tt10rkk7ogyco9b3mpueb;
       public          everest_admin    false    236    243    3630            b           2606    17531 0   test_run_test_result fkckep0y66tbakh4krbhisxbdom    FK CONSTRAINT     �   ALTER TABLE ONLY public.test_run_test_result
    ADD CONSTRAINT fkckep0y66tbakh4krbhisxbdom FOREIGN KEY (test_run_test_results_id) REFERENCES public.test_run(id);
 Z   ALTER TABLE ONLY public.test_run_test_result DROP CONSTRAINT fkckep0y66tbakh4krbhisxbdom;
       public          everest_admin    false    246    247    3640            ^           2606    17511 9   test_iteration_iteration_step fkd2v1cpt7rug1iuhwcfhnf38s2    FK CONSTRAINT     �   ALTER TABLE ONLY public.test_iteration_iteration_step
    ADD CONSTRAINT fkd2v1cpt7rug1iuhwcfhnf38s2 FOREIGN KEY (iteration_step_id) REFERENCES public.iteration_step(id);
 c   ALTER TABLE ONLY public.test_iteration_iteration_step DROP CONSTRAINT fkd2v1cpt7rug1iuhwcfhnf38s2;
       public          everest_admin    false    222    244    3600            Y           2606    17486 4   test_cycle_test_case_ids fkdbiqaqks2t9i0ftwighc4puqk    FK CONSTRAINT     �   ALTER TABLE ONLY public.test_cycle_test_case_ids
    ADD CONSTRAINT fkdbiqaqks2t9i0ftwighc4puqk FOREIGN KEY (test_cycle_id) REFERENCES public.test_cycle(id);
 ^   ALTER TABLE ONLY public.test_cycle_test_case_ids DROP CONSTRAINT fkdbiqaqks2t9i0ftwighc4puqk;
       public          everest_admin    false    241    3632    240            Z           2606    17491 &   test_group fkexhynrfen81eaqi6a0s8ssch3    FK CONSTRAINT     �   ALTER TABLE ONLY public.test_group
    ADD CONSTRAINT fkexhynrfen81eaqi6a0s8ssch3 FOREIGN KEY (project_id) REFERENCES public.project(id);
 P   ALTER TABLE ONLY public.test_group DROP CONSTRAINT fkexhynrfen81eaqi6a0s8ssch3;
       public          everest_admin    false    3610    226    242            U           2606    17466 1   test_case_test_groups fkgh822uqx2oba5m9j6swv3fxvm    FK CONSTRAINT     �   ALTER TABLE ONLY public.test_case_test_groups
    ADD CONSTRAINT fkgh822uqx2oba5m9j6swv3fxvm FOREIGN KEY (test_case_id) REFERENCES public.test_case(id);
 [   ALTER TABLE ONLY public.test_case_test_groups DROP CONSTRAINT fkgh822uqx2oba5m9j6swv3fxvm;
       public          everest_admin    false    3630    236    239            J           2606    17406 $   scenario fkgk8xds3ylwmbls60s1chl8ssc    FK CONSTRAINT     �   ALTER TABLE ONLY public.scenario
    ADD CONSTRAINT fkgk8xds3ylwmbls60s1chl8ssc FOREIGN KEY (project_id) REFERENCES public.project(id);
 N   ALTER TABLE ONLY public.scenario DROP CONSTRAINT fkgk8xds3ylwmbls60s1chl8ssc;
       public          everest_admin    false    226    3610    232            >           2606    17351 #   bug_step fkh36t02m1dag5n7q0ons4jqck    FK CONSTRAINT     �   ALTER TABLE ONLY public.bug_step
    ADD CONSTRAINT fkh36t02m1dag5n7q0ons4jqck FOREIGN KEY (step_id) REFERENCES public.step(id);
 M   ALTER TABLE ONLY public.bug_step DROP CONSTRAINT fkh36t02m1dag5n7q0ons4jqck;
       public          everest_admin    false    234    3626    220            @           2606    17361 '   person_role fkhyx1efsls0f00lxs6xd4w2b3j    FK CONSTRAINT     �   ALTER TABLE ONLY public.person_role
    ADD CONSTRAINT fkhyx1efsls0f00lxs6xd4w2b3j FOREIGN KEY (person_id) REFERENCES public.person(id);
 Q   ALTER TABLE ONLY public.person_role DROP CONSTRAINT fkhyx1efsls0f00lxs6xd4w2b3j;
       public          everest_admin    false    224    225    3604            <           2606    17336    bug fki91cmp5c6v9yv26iow1g0emxc    FK CONSTRAINT     �   ALTER TABLE ONLY public.bug
    ADD CONSTRAINT fki91cmp5c6v9yv26iow1g0emxc FOREIGN KEY (project_id) REFERENCES public.project(id);
 I   ALTER TABLE ONLY public.bug DROP CONSTRAINT fki91cmp5c6v9yv26iow1g0emxc;
       public          everest_admin    false    226    218    3610            Q           2606    17441 %   test_case fkit9gxtn7qhwml7ni05l11syb8    FK CONSTRAINT     �   ALTER TABLE ONLY public.test_case
    ADD CONSTRAINT fkit9gxtn7qhwml7ni05l11syb8 FOREIGN KEY (project_id) REFERENCES public.project(id);
 O   ALTER TABLE ONLY public.test_case DROP CONSTRAINT fkit9gxtn7qhwml7ni05l11syb8;
       public          everest_admin    false    236    226    3610            =           2606    17346 +   bug_environment fkjvuapln2f2nlgf8mnuognc310    FK CONSTRAINT     �   ALTER TABLE ONLY public.bug_environment
    ADD CONSTRAINT fkjvuapln2f2nlgf8mnuognc310 FOREIGN KEY (environment_id) REFERENCES public.environment(id);
 U   ALTER TABLE ONLY public.bug_environment DROP CONSTRAINT fkjvuapln2f2nlgf8mnuognc310;
       public          everest_admin    false    3598    219    221            D           2606    17381 /   project_environment fkl74d0tcnx5nulhfueh8xysjic    FK CONSTRAINT     �   ALTER TABLE ONLY public.project_environment
    ADD CONSTRAINT fkl74d0tcnx5nulhfueh8xysjic FOREIGN KEY (environment_id) REFERENCES public.environment(id);
 Y   ALTER TABLE ONLY public.project_environment DROP CONSTRAINT fkl74d0tcnx5nulhfueh8xysjic;
       public          everest_admin    false    228    3598    221            K           2606    17416 0   scenario_environment fkl8u6rrw8uirq8jxsa9l0uxerg    FK CONSTRAINT     �   ALTER TABLE ONLY public.scenario_environment
    ADD CONSTRAINT fkl8u6rrw8uirq8jxsa9l0uxerg FOREIGN KEY (environment_id) REFERENCES public.environment(id);
 Z   ALTER TABLE ONLY public.scenario_environment DROP CONSTRAINT fkl8u6rrw8uirq8jxsa9l0uxerg;
       public          everest_admin    false    221    3598    233            _           2606    17516 '   test_result fkm2rib6nvod8yq4rp3jsg1mf4l    FK CONSTRAINT     �   ALTER TABLE ONLY public.test_result
    ADD CONSTRAINT fkm2rib6nvod8yq4rp3jsg1mf4l FOREIGN KEY (automated_test_id) REFERENCES public.automated_test(id);
 Q   ALTER TABLE ONLY public.test_result DROP CONSTRAINT fkm2rib6nvod8yq4rp3jsg1mf4l;
       public          everest_admin    false    245    3592    217            ]           2606    17496 *   test_iteration fkm4mccqoa1hnrjktjkrk4dii21    FK CONSTRAINT     �   ALTER TABLE ONLY public.test_iteration
    ADD CONSTRAINT fkm4mccqoa1hnrjktjkrk4dii21 FOREIGN KEY (test_cycle_id) REFERENCES public.test_cycle(id);
 T   ALTER TABLE ONLY public.test_iteration DROP CONSTRAINT fkm4mccqoa1hnrjktjkrk4dii21;
       public          everest_admin    false    240    243    3632            `           2606    17521 $   test_run fkn565mnq0951li4yvods05cjod    FK CONSTRAINT     �   ALTER TABLE ONLY public.test_run
    ADD CONSTRAINT fkn565mnq0951li4yvods05cjod FOREIGN KEY (project_id) REFERENCES public.project(id);
 N   ALTER TABLE ONLY public.test_run DROP CONSTRAINT fkn565mnq0951li4yvods05cjod;
       public          everest_admin    false    246    3610    226            S           2606    17451 1   test_case_environment fknicc7xvcirwj0kqmhqxsgj3mp    FK CONSTRAINT     �   ALTER TABLE ONLY public.test_case_environment
    ADD CONSTRAINT fknicc7xvcirwj0kqmhqxsgj3mp FOREIGN KEY (environment_id) REFERENCES public.environment(id);
 [   ALTER TABLE ONLY public.test_case_environment DROP CONSTRAINT fknicc7xvcirwj0kqmhqxsgj3mp;
       public          everest_admin    false    221    237    3598            ?           2606    17356     link fkns4w5peakgt35y9cg16fi2n6t    FK CONSTRAINT     �   ALTER TABLE ONLY public.link
    ADD CONSTRAINT fkns4w5peakgt35y9cg16fi2n6t FOREIGN KEY (project_id) REFERENCES public.project(id);
 J   ALTER TABLE ONLY public.link DROP CONSTRAINT fkns4w5peakgt35y9cg16fi2n6t;
       public          everest_admin    false    223    3610    226            E           2606    17386 /   project_environment fkohbou9h8qmqvydblecd85d1y1    FK CONSTRAINT     �   ALTER TABLE ONLY public.project_environment
    ADD CONSTRAINT fkohbou9h8qmqvydblecd85d1y1 FOREIGN KEY (project_environments_id) REFERENCES public.project(id);
 Y   ALTER TABLE ONLY public.project_environment DROP CONSTRAINT fkohbou9h8qmqvydblecd85d1y1;
       public          everest_admin    false    228    226    3610            V           2606    17471 0   test_case_test_groups fkohp0n1i8nxh8226dfiu9tte7    FK CONSTRAINT     �   ALTER TABLE ONLY public.test_case_test_groups
    ADD CONSTRAINT fkohp0n1i8nxh8226dfiu9tte7 FOREIGN KEY (test_group_id) REFERENCES public.test_group(id);
 Z   ALTER TABLE ONLY public.test_case_test_groups DROP CONSTRAINT fkohp0n1i8nxh8226dfiu9tte7;
       public          everest_admin    false    3634    239    242            W           2606    17481 &   test_cycle fkpx73o7axguyhq6debxatfvxmf    FK CONSTRAINT     �   ALTER TABLE ONLY public.test_cycle
    ADD CONSTRAINT fkpx73o7axguyhq6debxatfvxmf FOREIGN KEY (release_plan_id) REFERENCES public.release_plan(id);
 P   ALTER TABLE ONLY public.test_cycle DROP CONSTRAINT fkpx73o7axguyhq6debxatfvxmf;
       public          everest_admin    false    240    230    3618            C           2606    17376 (   project_area fks14e0fppv93u8edomxmxy6kkm    FK CONSTRAINT     �   ALTER TABLE ONLY public.project_area
    ADD CONSTRAINT fks14e0fppv93u8edomxmxy6kkm FOREIGN KEY (project_areas_id) REFERENCES public.project(id);
 R   ALTER TABLE ONLY public.project_area DROP CONSTRAINT fks14e0fppv93u8edomxmxy6kkm;
       public          everest_admin    false    3610    226    227            A           2606    17366 '   person_role fks7asxi8amiwjjq1sonlc4rihn    FK CONSTRAINT     �   ALTER TABLE ONLY public.person_role
    ADD CONSTRAINT fks7asxi8amiwjjq1sonlc4rihn FOREIGN KEY (role_id) REFERENCES public.role(id);
 Q   ALTER TABLE ONLY public.person_role DROP CONSTRAINT fks7asxi8amiwjjq1sonlc4rihn;
       public          everest_admin    false    225    3620    231            X           2606    17476 &   test_cycle fktch3psrkx6lpc3byyppdhlnoi    FK CONSTRAINT     �   ALTER TABLE ONLY public.test_cycle
    ADD CONSTRAINT fktch3psrkx6lpc3byyppdhlnoi FOREIGN KEY (environ_id) REFERENCES public.environment(id);
 P   ALTER TABLE ONLY public.test_cycle DROP CONSTRAINT fktch3psrkx6lpc3byyppdhlnoi;
       public          everest_admin    false    221    3598    240            �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �   n   x�3�4�L���ʂ�Z�DC���D}�K�D�������R������p=敖�
/?��Ĭ��¢�rC����
��L�1%���)��y@���������� ��$      �      x�3�4����� �%      �      x������ � �      �      x������ � �      �      x������ � �             x������ � �            x������ � �         A   x�3�4���q�wr�t�2�q�\]���|"��aBA�^��!�.��~\&0aǀ �P� $K�            x������ � �            x������ � �            x������ � �            x������ � �            x������ � �            x������ � �      	      x������ � �      
      x������ � �            x������ � �            x������ � �            x������ � �            x������ � �            x������ � �            x������ � �            x������ � �            x������ � �     