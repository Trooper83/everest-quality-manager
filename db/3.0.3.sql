--
-- PostgreSQL database dump
--

-- Dumped from database version 16.2 (Postgres.app)
-- Dumped by pg_dump version 16.1

-- Started on 2024-03-31 09:10:44 PDT

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 216 (class 1259 OID 18797)
-- Name: area; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.area (
    id bigint NOT NULL,
    version bigint NOT NULL,
    name character varying(100) NOT NULL
);


ALTER TABLE public.area OWNER TO everest_admin;

--
-- TOC entry 217 (class 1259 OID 18802)
-- Name: automated_test; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.automated_test (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    name character varying(255),
    full_name character varying(500) NOT NULL,
    project_id bigint NOT NULL
);


ALTER TABLE public.automated_test OWNER TO everest_admin;

--
-- TOC entry 218 (class 1259 OID 18809)
-- Name: bug; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.bug (
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


ALTER TABLE public.bug OWNER TO everest_admin;

--
-- TOC entry 219 (class 1259 OID 18816)
-- Name: bug_environment; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.bug_environment (
    bug_environments_id bigint,
    environment_id bigint,
    environments_idx integer
);


ALTER TABLE public.bug_environment OWNER TO everest_admin;

--
-- TOC entry 220 (class 1259 OID 18819)
-- Name: bug_step; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.bug_step (
    bug_steps_id bigint,
    step_id bigint,
    steps_idx integer
);


ALTER TABLE public.bug_step OWNER TO everest_admin;

--
-- TOC entry 221 (class 1259 OID 18822)
-- Name: environment; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.environment (
    id bigint NOT NULL,
    version bigint NOT NULL,
    name character varying(100) NOT NULL
);


ALTER TABLE public.environment OWNER TO everest_admin;

--
-- TOC entry 215 (class 1259 OID 18796)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: everest_admin
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.hibernate_sequence OWNER TO everest_admin;

--
-- TOC entry 222 (class 1259 OID 18827)
-- Name: iteration_step; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.iteration_step (
    id bigint NOT NULL,
    version bigint NOT NULL,
    act character varying(500),
    data character varying(500),
    result character varying(500)
);


ALTER TABLE public.iteration_step OWNER TO everest_admin;

--
-- TOC entry 223 (class 1259 OID 18834)
-- Name: link; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.link (
    id bigint NOT NULL,
    version bigint NOT NULL,
    owner_id bigint NOT NULL,
    project_id bigint NOT NULL,
    linked_id bigint NOT NULL,
    relation character varying(13) NOT NULL
);


ALTER TABLE public.link OWNER TO everest_admin;

--
-- TOC entry 224 (class 1259 OID 18839)
-- Name: person; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.person (
    id bigint NOT NULL,
    version bigint NOT NULL,
    password_expired boolean NOT NULL,
    account_locked boolean NOT NULL,
    password character varying(256) NOT NULL,
    account_expired boolean NOT NULL,
    enabled boolean NOT NULL,
    email character varying(255) NOT NULL
);


ALTER TABLE public.person OWNER TO everest_admin;

--
-- TOC entry 225 (class 1259 OID 18846)
-- Name: person_role; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.person_role (
    person_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE public.person_role OWNER TO everest_admin;

--
-- TOC entry 226 (class 1259 OID 18851)
-- Name: project; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.project (
    id bigint NOT NULL,
    version bigint NOT NULL,
    code character varying(5) NOT NULL,
    name character varying(100) NOT NULL
);


ALTER TABLE public.project OWNER TO everest_admin;

--
-- TOC entry 227 (class 1259 OID 18856)
-- Name: project_area; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.project_area (
    project_areas_id bigint NOT NULL,
    area_id bigint
);


ALTER TABLE public.project_area OWNER TO everest_admin;

--
-- TOC entry 228 (class 1259 OID 18859)
-- Name: project_environment; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.project_environment (
    project_environments_id bigint NOT NULL,
    environment_id bigint
);


ALTER TABLE public.project_environment OWNER TO everest_admin;

--
-- TOC entry 229 (class 1259 OID 18862)
-- Name: registration_code; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.registration_code (
    id bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    username character varying(255) NOT NULL,
    token character varying(255) NOT NULL
);


ALTER TABLE public.registration_code OWNER TO everest_admin;

--
-- TOC entry 230 (class 1259 OID 18869)
-- Name: release_plan; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.release_plan (
    id bigint NOT NULL,
    version bigint NOT NULL,
    person_id bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    name character varying(500) NOT NULL,
    release_date timestamp without time zone,
    notes character varying(1000),
    status character varying(11) NOT NULL,
    planned_date timestamp without time zone,
    project_id bigint NOT NULL
);


ALTER TABLE public.release_plan OWNER TO everest_admin;

--
-- TOC entry 231 (class 1259 OID 18876)
-- Name: role; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.role (
    id bigint NOT NULL,
    version bigint NOT NULL,
    authority character varying(255) NOT NULL
);


ALTER TABLE public.role OWNER TO everest_admin;

--
-- TOC entry 232 (class 1259 OID 18881)
-- Name: scenario; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.scenario (
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


ALTER TABLE public.scenario OWNER TO everest_admin;

--
-- TOC entry 233 (class 1259 OID 18888)
-- Name: scenario_environment; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.scenario_environment (
    scenario_environments_id bigint,
    environment_id bigint,
    environments_idx integer
);


ALTER TABLE public.scenario_environment OWNER TO everest_admin;

--
-- TOC entry 234 (class 1259 OID 18891)
-- Name: step; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.step (
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


ALTER TABLE public.step OWNER TO everest_admin;

--
-- TOC entry 235 (class 1259 OID 18898)
-- Name: step_template; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.step_template (
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


ALTER TABLE public.step_template OWNER TO everest_admin;

--
-- TOC entry 236 (class 1259 OID 18905)
-- Name: test_case; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.test_case (
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


ALTER TABLE public.test_case OWNER TO everest_admin;

--
-- TOC entry 237 (class 1259 OID 18912)
-- Name: test_case_environment; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.test_case_environment (
    test_case_environments_id bigint NOT NULL,
    environment_id bigint
);


ALTER TABLE public.test_case_environment OWNER TO everest_admin;

--
-- TOC entry 238 (class 1259 OID 18915)
-- Name: test_case_step; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.test_case_step (
    test_case_steps_id bigint,
    step_id bigint,
    steps_idx integer
);


ALTER TABLE public.test_case_step OWNER TO everest_admin;

--
-- TOC entry 239 (class 1259 OID 18918)
-- Name: test_case_test_groups; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.test_case_test_groups (
    test_group_id bigint NOT NULL,
    test_case_id bigint NOT NULL,
    test_groups_idx integer
);


ALTER TABLE public.test_case_test_groups OWNER TO everest_admin;

--
-- TOC entry 240 (class 1259 OID 18921)
-- Name: test_cycle; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.test_cycle (
    id bigint NOT NULL,
    version bigint NOT NULL,
    environ_id bigint,
    date_created timestamp without time zone NOT NULL,
    name character varying(500) NOT NULL,
    platform character varying(7),
    release_plan_id bigint NOT NULL
);


ALTER TABLE public.test_cycle OWNER TO everest_admin;

--
-- TOC entry 241 (class 1259 OID 18928)
-- Name: test_cycle_test_case_ids; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.test_cycle_test_case_ids (
    test_cycle_id bigint,
    test_case_ids_long bigint
);


ALTER TABLE public.test_cycle_test_case_ids OWNER TO everest_admin;

--
-- TOC entry 242 (class 1259 OID 18931)
-- Name: test_group; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.test_group (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    name character varying(255) NOT NULL,
    project_id bigint NOT NULL
);


ALTER TABLE public.test_group OWNER TO everest_admin;

--
-- TOC entry 243 (class 1259 OID 18936)
-- Name: test_iteration; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.test_iteration (
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


ALTER TABLE public.test_iteration OWNER TO everest_admin;

--
-- TOC entry 244 (class 1259 OID 18943)
-- Name: test_iteration_iteration_step; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.test_iteration_iteration_step (
    test_iteration_steps_id bigint NOT NULL,
    iteration_step_id bigint,
    steps_idx integer
);


ALTER TABLE public.test_iteration_iteration_step OWNER TO everest_admin;

--
-- TOC entry 245 (class 1259 OID 18946)
-- Name: test_result; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.test_result (
    id bigint NOT NULL,
    version bigint NOT NULL,
    failure_cause character varying(2500),
    date_created timestamp without time zone NOT NULL,
    automated_test_id bigint NOT NULL,
    test_run_id bigint NOT NULL,
    result character varying(7) NOT NULL
);


ALTER TABLE public.test_result OWNER TO everest_admin;

--
-- TOC entry 246 (class 1259 OID 18953)
-- Name: test_run; Type: TABLE; Schema: public; Owner: everest_admin
--

CREATE TABLE public.test_run (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    name character varying(255) NOT NULL,
    project_id bigint NOT NULL
);


ALTER TABLE public.test_run OWNER TO everest_admin;

--
-- TOC entry 3822 (class 0 OID 18797)
-- Dependencies: 216
-- Data for Name: area; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.area (id, version, name) FROM stdin;
\.


--
-- TOC entry 3823 (class 0 OID 18802)
-- Dependencies: 217
-- Data for Name: automated_test; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.automated_test (id, version, date_created, name, full_name, project_id) FROM stdin;
\.


--
-- TOC entry 3824 (class 0 OID 18809)
-- Dependencies: 218
-- Data for Name: bug; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.bug (id, version, date_created, last_updated, platform, notes, person_id, name, actual, status, expected, description, project_id, area_id) FROM stdin;
\.


--
-- TOC entry 3825 (class 0 OID 18816)
-- Dependencies: 219
-- Data for Name: bug_environment; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.bug_environment (bug_environments_id, environment_id, environments_idx) FROM stdin;
\.


--
-- TOC entry 3826 (class 0 OID 18819)
-- Dependencies: 220
-- Data for Name: bug_step; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.bug_step (bug_steps_id, step_id, steps_idx) FROM stdin;
\.


--
-- TOC entry 3827 (class 0 OID 18822)
-- Dependencies: 221
-- Data for Name: environment; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.environment (id, version, name) FROM stdin;
\.


--
-- TOC entry 3828 (class 0 OID 18827)
-- Dependencies: 222
-- Data for Name: iteration_step; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.iteration_step (id, version, act, data, result) FROM stdin;
\.


--
-- TOC entry 3829 (class 0 OID 18834)
-- Dependencies: 223
-- Data for Name: link; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.link (id, version, owner_id, project_id, linked_id, relation) FROM stdin;
\.


--
-- TOC entry 3830 (class 0 OID 18839)
-- Dependencies: 224
-- Data for Name: person; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.person (id, version, password_expired, account_locked, password, account_expired, enabled, email) FROM stdin;
5	0	f	f	{bcrypt}$2a$10$DgaMMDElCTd9NLY.T8oWBO4YHMg.IeC6psr6AQE2XSPXXvZHWVEA2	f	t	app_admin@appadmin.com
\.


--
-- TOC entry 3831 (class 0 OID 18846)
-- Dependencies: 225
-- Data for Name: person_role; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.person_role (person_id, role_id) FROM stdin;
5	4
\.


--
-- TOC entry 3832 (class 0 OID 18851)
-- Dependencies: 226
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.project (id, version, code, name) FROM stdin;
\.


--
-- TOC entry 3833 (class 0 OID 18856)
-- Dependencies: 227
-- Data for Name: project_area; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.project_area (project_areas_id, area_id) FROM stdin;
\.


--
-- TOC entry 3834 (class 0 OID 18859)
-- Dependencies: 228
-- Data for Name: project_environment; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.project_environment (project_environments_id, environment_id) FROM stdin;
\.


--
-- TOC entry 3835 (class 0 OID 18862)
-- Dependencies: 229
-- Data for Name: registration_code; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.registration_code (id, date_created, username, token) FROM stdin;
\.


--
-- TOC entry 3836 (class 0 OID 18869)
-- Dependencies: 230
-- Data for Name: release_plan; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.release_plan (id, version, person_id, date_created, last_updated, name, release_date, notes, status, planned_date, project_id) FROM stdin;
\.


--
-- TOC entry 3837 (class 0 OID 18876)
-- Dependencies: 231
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.role (id, version, authority) FROM stdin;
1	0	ROLE_BASIC
2	0	ROLE_READ_ONLY
3	0	ROLE_PROJECT_ADMIN
4	0	ROLE_APP_ADMIN
\.


--
-- TOC entry 3838 (class 0 OID 18881)
-- Dependencies: 232
-- Data for Name: scenario; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.scenario (id, version, date_created, last_updated, platform, person_id, execution_method, name, type, gherkin, description, project_id, area_id) FROM stdin;
\.


--
-- TOC entry 3839 (class 0 OID 18888)
-- Dependencies: 233
-- Data for Name: scenario_environment; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.scenario_environment (scenario_environments_id, environment_id, environments_idx) FROM stdin;
\.


--
-- TOC entry 3840 (class 0 OID 18891)
-- Dependencies: 234
-- Data for Name: step; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.step (id, version, act, template_id, date_created, last_updated, is_builder_step, data, result) FROM stdin;
\.


--
-- TOC entry 3841 (class 0 OID 18898)
-- Dependencies: 235
-- Data for Name: step_template; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.step_template (id, version, act, person_id, date_created, last_updated, name, result, project_id) FROM stdin;
\.


--
-- TOC entry 3842 (class 0 OID 18905)
-- Dependencies: 236
-- Data for Name: test_case; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.test_case (id, version, date_created, last_updated, platform, person_id, execution_method, name, type, description, project_id, verify, area_id) FROM stdin;
\.


--
-- TOC entry 3843 (class 0 OID 18912)
-- Dependencies: 237
-- Data for Name: test_case_environment; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.test_case_environment (test_case_environments_id, environment_id) FROM stdin;
\.


--
-- TOC entry 3844 (class 0 OID 18915)
-- Dependencies: 238
-- Data for Name: test_case_step; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.test_case_step (test_case_steps_id, step_id, steps_idx) FROM stdin;
\.


--
-- TOC entry 3845 (class 0 OID 18918)
-- Dependencies: 239
-- Data for Name: test_case_test_groups; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.test_case_test_groups (test_group_id, test_case_id, test_groups_idx) FROM stdin;
\.


--
-- TOC entry 3846 (class 0 OID 18921)
-- Dependencies: 240
-- Data for Name: test_cycle; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.test_cycle (id, version, environ_id, date_created, name, platform, release_plan_id) FROM stdin;
\.


--
-- TOC entry 3847 (class 0 OID 18928)
-- Dependencies: 241
-- Data for Name: test_cycle_test_case_ids; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.test_cycle_test_case_ids (test_cycle_id, test_case_ids_long) FROM stdin;
\.


--
-- TOC entry 3848 (class 0 OID 18931)
-- Dependencies: 242
-- Data for Name: test_group; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.test_group (id, version, date_created, name, project_id) FROM stdin;
\.


--
-- TOC entry 3849 (class 0 OID 18936)
-- Dependencies: 243
-- Data for Name: test_iteration; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.test_iteration (id, version, date_created, last_updated, notes, result, test_cycle_id, person_id, date_executed, name, test_case_id, verify) FROM stdin;
\.


--
-- TOC entry 3850 (class 0 OID 18943)
-- Dependencies: 244
-- Data for Name: test_iteration_iteration_step; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.test_iteration_iteration_step (test_iteration_steps_id, iteration_step_id, steps_idx) FROM stdin;
\.


--
-- TOC entry 3851 (class 0 OID 18946)
-- Dependencies: 245
-- Data for Name: test_result; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.test_result (id, version, failure_cause, date_created, automated_test_id, test_run_id, result) FROM stdin;
\.


--
-- TOC entry 3852 (class 0 OID 18953)
-- Dependencies: 246
-- Data for Name: test_run; Type: TABLE DATA; Schema: public; Owner: everest_admin
--

COPY public.test_run (id, version, date_created, name, project_id) FROM stdin;
\.


--
-- TOC entry 3858 (class 0 OID 0)
-- Dependencies: 215
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: everest_admin
--

SELECT pg_catalog.setval('public.hibernate_sequence', 5, true);


--
-- TOC entry 3586 (class 2606 OID 18801)
-- Name: area area_pkey; Type: CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.area
    ADD CONSTRAINT area_pkey PRIMARY KEY (id);


--
-- TOC entry 3588 (class 2606 OID 18808)
-- Name: automated_test automated_test_pkey; Type: CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.automated_test
    ADD CONSTRAINT automated_test_pkey PRIMARY KEY (id);


--
-- TOC entry 3592 (class 2606 OID 18815)
-- Name: bug bug_pkey; Type: CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.bug
    ADD CONSTRAINT bug_pkey PRIMARY KEY (id);


--
-- TOC entry 3594 (class 2606 OID 18826)
-- Name: environment environment_pkey; Type: CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.environment
    ADD CONSTRAINT environment_pkey PRIMARY KEY (id);


--
-- TOC entry 3596 (class 2606 OID 18833)
-- Name: iteration_step iteration_step_pkey; Type: CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.iteration_step
    ADD CONSTRAINT iteration_step_pkey PRIMARY KEY (id);


--
-- TOC entry 3598 (class 2606 OID 18838)
-- Name: link link_pkey; Type: CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.link
    ADD CONSTRAINT link_pkey PRIMARY KEY (id);


--
-- TOC entry 3600 (class 2606 OID 18845)
-- Name: person person_pkey; Type: CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT person_pkey PRIMARY KEY (id);


--
-- TOC entry 3604 (class 2606 OID 18850)
-- Name: person_role person_role_pkey; Type: CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.person_role
    ADD CONSTRAINT person_role_pkey PRIMARY KEY (person_id, role_id);


--
-- TOC entry 3606 (class 2606 OID 18855)
-- Name: project project_pkey; Type: CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT project_pkey PRIMARY KEY (id);


--
-- TOC entry 3612 (class 2606 OID 18868)
-- Name: registration_code registration_code_pkey; Type: CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.registration_code
    ADD CONSTRAINT registration_code_pkey PRIMARY KEY (id);


--
-- TOC entry 3614 (class 2606 OID 18875)
-- Name: release_plan release_plan_pkey; Type: CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.release_plan
    ADD CONSTRAINT release_plan_pkey PRIMARY KEY (id);


--
-- TOC entry 3616 (class 2606 OID 18880)
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- TOC entry 3620 (class 2606 OID 18887)
-- Name: scenario scenario_pkey; Type: CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.scenario
    ADD CONSTRAINT scenario_pkey PRIMARY KEY (id);


--
-- TOC entry 3622 (class 2606 OID 18897)
-- Name: step step_pkey; Type: CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.step
    ADD CONSTRAINT step_pkey PRIMARY KEY (id);


--
-- TOC entry 3624 (class 2606 OID 18904)
-- Name: step_template step_template_pkey; Type: CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.step_template
    ADD CONSTRAINT step_template_pkey PRIMARY KEY (id);


--
-- TOC entry 3626 (class 2606 OID 18911)
-- Name: test_case test_case_pkey; Type: CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.test_case
    ADD CONSTRAINT test_case_pkey PRIMARY KEY (id);


--
-- TOC entry 3628 (class 2606 OID 18927)
-- Name: test_cycle test_cycle_pkey; Type: CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.test_cycle
    ADD CONSTRAINT test_cycle_pkey PRIMARY KEY (id);


--
-- TOC entry 3630 (class 2606 OID 18935)
-- Name: test_group test_group_pkey; Type: CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.test_group
    ADD CONSTRAINT test_group_pkey PRIMARY KEY (id);


--
-- TOC entry 3632 (class 2606 OID 18942)
-- Name: test_iteration test_iteration_pkey; Type: CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.test_iteration
    ADD CONSTRAINT test_iteration_pkey PRIMARY KEY (id);


--
-- TOC entry 3634 (class 2606 OID 18952)
-- Name: test_result test_result_pkey; Type: CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.test_result
    ADD CONSTRAINT test_result_pkey PRIMARY KEY (id);


--
-- TOC entry 3636 (class 2606 OID 18957)
-- Name: test_run test_run_pkey; Type: CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.test_run
    ADD CONSTRAINT test_run_pkey PRIMARY KEY (id);


--
-- TOC entry 3590 (class 2606 OID 18959)
-- Name: automated_test uk8a9d9878ace51399c8abd8b049d1; Type: CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.automated_test
    ADD CONSTRAINT uk8a9d9878ace51399c8abd8b049d1 UNIQUE (project_id, full_name);


--
-- TOC entry 3608 (class 2606 OID 18965)
-- Name: project uk_3k75vvu7mevyvvb5may5lj8k7; Type: CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT uk_3k75vvu7mevyvvb5may5lj8k7 UNIQUE (name);


--
-- TOC entry 3610 (class 2606 OID 18963)
-- Name: project uk_eh3nusutt0qy84a4yr9pfxkyg; Type: CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT uk_eh3nusutt0qy84a4yr9pfxkyg UNIQUE (code);


--
-- TOC entry 3602 (class 2606 OID 18961)
-- Name: person uk_fwmwi44u55bo4rvwsv0cln012; Type: CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT uk_fwmwi44u55bo4rvwsv0cln012 UNIQUE (email);


--
-- TOC entry 3618 (class 2606 OID 18967)
-- Name: role uk_irsamgnera6angm0prq1kemt2; Type: CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT uk_irsamgnera6angm0prq1kemt2 UNIQUE (authority);


--
-- TOC entry 3657 (class 2606 OID 19068)
-- Name: step_template fk38krdos2kx2pc2i4nr81yivhx; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.step_template
    ADD CONSTRAINT fk38krdos2kx2pc2i4nr81yivhx FOREIGN KEY (person_id) REFERENCES public.person(id);


--
-- TOC entry 3662 (class 2606 OID 19098)
-- Name: test_case_environment fk4ajnc7fbdc2iyj2k8ytmga4h0; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.test_case_environment
    ADD CONSTRAINT fk4ajnc7fbdc2iyj2k8ytmga4h0 FOREIGN KEY (test_case_environments_id) REFERENCES public.test_case(id);


--
-- TOC entry 3658 (class 2606 OID 19073)
-- Name: step_template fk50saotfxwhf1a26qoyfq8qyr9; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.step_template
    ADD CONSTRAINT fk50saotfxwhf1a26qoyfq8qyr9 FOREIGN KEY (project_id) REFERENCES public.project(id);


--
-- TOC entry 3659 (class 2606 OID 19088)
-- Name: test_case fk5a1omo3b7vn82yqckgn76cf5; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.test_case
    ADD CONSTRAINT fk5a1omo3b7vn82yqckgn76cf5 FOREIGN KEY (area_id) REFERENCES public.area(id);


--
-- TOC entry 3652 (class 2606 OID 19053)
-- Name: scenario fk6hp1q8kdvfd0bs6vtr5k5ddui; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.scenario
    ADD CONSTRAINT fk6hp1q8kdvfd0bs6vtr5k5ddui FOREIGN KEY (area_id) REFERENCES public.area(id);


--
-- TOC entry 3650 (class 2606 OID 19038)
-- Name: release_plan fk6y0f94nne0acxxhkpy2xy30uv; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.release_plan
    ADD CONSTRAINT fk6y0f94nne0acxxhkpy2xy30uv FOREIGN KEY (project_id) REFERENCES public.project(id);


--
-- TOC entry 3638 (class 2606 OID 18983)
-- Name: bug fk8hnx6x9kyglxi85uqadpw4cgd; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.bug
    ADD CONSTRAINT fk8hnx6x9kyglxi85uqadpw4cgd FOREIGN KEY (area_id) REFERENCES public.area(id);


--
-- TOC entry 3637 (class 2606 OID 18968)
-- Name: automated_test fk8vvu4v9uwyssf5pqv5l4bemb; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.automated_test
    ADD CONSTRAINT fk8vvu4v9uwyssf5pqv5l4bemb FOREIGN KEY (project_id) REFERENCES public.project(id);


--
-- TOC entry 3664 (class 2606 OID 19103)
-- Name: test_case_step fk9lnmhnxmhiopdnc8vdfg7sh1x; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.test_case_step
    ADD CONSTRAINT fk9lnmhnxmhiopdnc8vdfg7sh1x FOREIGN KEY (step_id) REFERENCES public.step(id);


--
-- TOC entry 3651 (class 2606 OID 19033)
-- Name: release_plan fk9pa4ds4owvxht9e0e73y6nanr; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.release_plan
    ADD CONSTRAINT fk9pa4ds4owvxht9e0e73y6nanr FOREIGN KEY (person_id) REFERENCES public.person(id);


--
-- TOC entry 3639 (class 2606 OID 18973)
-- Name: bug fkacv21dyrr01us0cwcf4erfil3; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.bug
    ADD CONSTRAINT fkacv21dyrr01us0cwcf4erfil3 FOREIGN KEY (person_id) REFERENCES public.person(id);


--
-- TOC entry 3653 (class 2606 OID 19043)
-- Name: scenario fkadgolvhsnmgr7cr2wv6x415pt; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.scenario
    ADD CONSTRAINT fkadgolvhsnmgr7cr2wv6x415pt FOREIGN KEY (person_id) REFERENCES public.person(id);


--
-- TOC entry 3671 (class 2606 OID 19143)
-- Name: test_iteration fkahonylro8iob3xuqpjf3xknlr; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.test_iteration
    ADD CONSTRAINT fkahonylro8iob3xuqpjf3xknlr FOREIGN KEY (person_id) REFERENCES public.person(id);


--
-- TOC entry 3656 (class 2606 OID 19063)
-- Name: step fkalfmhcod1k10n8j35fbt26wlb; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.step
    ADD CONSTRAINT fkalfmhcod1k10n8j35fbt26wlb FOREIGN KEY (template_id) REFERENCES public.step_template(id);


--
-- TOC entry 3646 (class 2606 OID 19013)
-- Name: project_area fkbf6m2u4tapd9cenngqjyqc774; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.project_area
    ADD CONSTRAINT fkbf6m2u4tapd9cenngqjyqc774 FOREIGN KEY (area_id) REFERENCES public.area(id);


--
-- TOC entry 3660 (class 2606 OID 19078)
-- Name: test_case fkbxo28q05096mw3d323ty0bemf; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.test_case
    ADD CONSTRAINT fkbxo28q05096mw3d323ty0bemf FOREIGN KEY (person_id) REFERENCES public.person(id);


--
-- TOC entry 3672 (class 2606 OID 19148)
-- Name: test_iteration fkcit7tt10rkk7ogyco9b3mpueb; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.test_iteration
    ADD CONSTRAINT fkcit7tt10rkk7ogyco9b3mpueb FOREIGN KEY (test_case_id) REFERENCES public.test_case(id);


--
-- TOC entry 3674 (class 2606 OID 19153)
-- Name: test_iteration_iteration_step fkd2v1cpt7rug1iuhwcfhnf38s2; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.test_iteration_iteration_step
    ADD CONSTRAINT fkd2v1cpt7rug1iuhwcfhnf38s2 FOREIGN KEY (iteration_step_id) REFERENCES public.iteration_step(id);


--
-- TOC entry 3669 (class 2606 OID 19128)
-- Name: test_cycle_test_case_ids fkdbiqaqks2t9i0ftwighc4puqk; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.test_cycle_test_case_ids
    ADD CONSTRAINT fkdbiqaqks2t9i0ftwighc4puqk FOREIGN KEY (test_cycle_id) REFERENCES public.test_cycle(id);


--
-- TOC entry 3670 (class 2606 OID 19133)
-- Name: test_group fkexhynrfen81eaqi6a0s8ssch3; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.test_group
    ADD CONSTRAINT fkexhynrfen81eaqi6a0s8ssch3 FOREIGN KEY (project_id) REFERENCES public.project(id);


--
-- TOC entry 3665 (class 2606 OID 19108)
-- Name: test_case_test_groups fkgh822uqx2oba5m9j6swv3fxvm; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.test_case_test_groups
    ADD CONSTRAINT fkgh822uqx2oba5m9j6swv3fxvm FOREIGN KEY (test_case_id) REFERENCES public.test_case(id);


--
-- TOC entry 3654 (class 2606 OID 19048)
-- Name: scenario fkgk8xds3ylwmbls60s1chl8ssc; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.scenario
    ADD CONSTRAINT fkgk8xds3ylwmbls60s1chl8ssc FOREIGN KEY (project_id) REFERENCES public.project(id);


--
-- TOC entry 3642 (class 2606 OID 18993)
-- Name: bug_step fkh36t02m1dag5n7q0ons4jqck; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.bug_step
    ADD CONSTRAINT fkh36t02m1dag5n7q0ons4jqck FOREIGN KEY (step_id) REFERENCES public.step(id);


--
-- TOC entry 3644 (class 2606 OID 19003)
-- Name: person_role fkhyx1efsls0f00lxs6xd4w2b3j; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.person_role
    ADD CONSTRAINT fkhyx1efsls0f00lxs6xd4w2b3j FOREIGN KEY (person_id) REFERENCES public.person(id);


--
-- TOC entry 3640 (class 2606 OID 18978)
-- Name: bug fki91cmp5c6v9yv26iow1g0emxc; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.bug
    ADD CONSTRAINT fki91cmp5c6v9yv26iow1g0emxc FOREIGN KEY (project_id) REFERENCES public.project(id);


--
-- TOC entry 3661 (class 2606 OID 19083)
-- Name: test_case fkit9gxtn7qhwml7ni05l11syb8; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.test_case
    ADD CONSTRAINT fkit9gxtn7qhwml7ni05l11syb8 FOREIGN KEY (project_id) REFERENCES public.project(id);


--
-- TOC entry 3641 (class 2606 OID 18988)
-- Name: bug_environment fkjvuapln2f2nlgf8mnuognc310; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.bug_environment
    ADD CONSTRAINT fkjvuapln2f2nlgf8mnuognc310 FOREIGN KEY (environment_id) REFERENCES public.environment(id);


--
-- TOC entry 3648 (class 2606 OID 19023)
-- Name: project_environment fkl74d0tcnx5nulhfueh8xysjic; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.project_environment
    ADD CONSTRAINT fkl74d0tcnx5nulhfueh8xysjic FOREIGN KEY (environment_id) REFERENCES public.environment(id);


--
-- TOC entry 3655 (class 2606 OID 19058)
-- Name: scenario_environment fkl8u6rrw8uirq8jxsa9l0uxerg; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.scenario_environment
    ADD CONSTRAINT fkl8u6rrw8uirq8jxsa9l0uxerg FOREIGN KEY (environment_id) REFERENCES public.environment(id);


--
-- TOC entry 3675 (class 2606 OID 19158)
-- Name: test_result fkm2rib6nvod8yq4rp3jsg1mf4l; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.test_result
    ADD CONSTRAINT fkm2rib6nvod8yq4rp3jsg1mf4l FOREIGN KEY (automated_test_id) REFERENCES public.automated_test(id);


--
-- TOC entry 3673 (class 2606 OID 19138)
-- Name: test_iteration fkm4mccqoa1hnrjktjkrk4dii21; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.test_iteration
    ADD CONSTRAINT fkm4mccqoa1hnrjktjkrk4dii21 FOREIGN KEY (test_cycle_id) REFERENCES public.test_cycle(id);


--
-- TOC entry 3677 (class 2606 OID 19168)
-- Name: test_run fkn565mnq0951li4yvods05cjod; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.test_run
    ADD CONSTRAINT fkn565mnq0951li4yvods05cjod FOREIGN KEY (project_id) REFERENCES public.project(id);


--
-- TOC entry 3663 (class 2606 OID 19093)
-- Name: test_case_environment fknicc7xvcirwj0kqmhqxsgj3mp; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.test_case_environment
    ADD CONSTRAINT fknicc7xvcirwj0kqmhqxsgj3mp FOREIGN KEY (environment_id) REFERENCES public.environment(id);


--
-- TOC entry 3643 (class 2606 OID 18998)
-- Name: link fkns4w5peakgt35y9cg16fi2n6t; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.link
    ADD CONSTRAINT fkns4w5peakgt35y9cg16fi2n6t FOREIGN KEY (project_id) REFERENCES public.project(id);


--
-- TOC entry 3649 (class 2606 OID 19028)
-- Name: project_environment fkohbou9h8qmqvydblecd85d1y1; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.project_environment
    ADD CONSTRAINT fkohbou9h8qmqvydblecd85d1y1 FOREIGN KEY (project_environments_id) REFERENCES public.project(id);


--
-- TOC entry 3666 (class 2606 OID 19113)
-- Name: test_case_test_groups fkohp0n1i8nxh8226dfiu9tte7; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.test_case_test_groups
    ADD CONSTRAINT fkohp0n1i8nxh8226dfiu9tte7 FOREIGN KEY (test_group_id) REFERENCES public.test_group(id);


--
-- TOC entry 3676 (class 2606 OID 19163)
-- Name: test_result fkoqjh3usd009un5fv5wmg7vv96; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.test_result
    ADD CONSTRAINT fkoqjh3usd009un5fv5wmg7vv96 FOREIGN KEY (test_run_id) REFERENCES public.test_run(id);


--
-- TOC entry 3667 (class 2606 OID 19123)
-- Name: test_cycle fkpx73o7axguyhq6debxatfvxmf; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.test_cycle
    ADD CONSTRAINT fkpx73o7axguyhq6debxatfvxmf FOREIGN KEY (release_plan_id) REFERENCES public.release_plan(id);


--
-- TOC entry 3647 (class 2606 OID 19018)
-- Name: project_area fks14e0fppv93u8edomxmxy6kkm; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.project_area
    ADD CONSTRAINT fks14e0fppv93u8edomxmxy6kkm FOREIGN KEY (project_areas_id) REFERENCES public.project(id);


--
-- TOC entry 3645 (class 2606 OID 19008)
-- Name: person_role fks7asxi8amiwjjq1sonlc4rihn; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.person_role
    ADD CONSTRAINT fks7asxi8amiwjjq1sonlc4rihn FOREIGN KEY (role_id) REFERENCES public.role(id);


--
-- TOC entry 3668 (class 2606 OID 19118)
-- Name: test_cycle fktch3psrkx6lpc3byyppdhlnoi; Type: FK CONSTRAINT; Schema: public; Owner: everest_admin
--

ALTER TABLE ONLY public.test_cycle
    ADD CONSTRAINT fktch3psrkx6lpc3byyppdhlnoi FOREIGN KEY (environ_id) REFERENCES public.environment(id);


-- Completed on 2024-03-31 09:10:44 PDT

--
-- PostgreSQL database dump complete
--

