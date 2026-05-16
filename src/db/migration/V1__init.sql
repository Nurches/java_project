CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE users (
    id VARCHAR(50) PRIMARY KEY,
    login VARCHAR(100) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    role_id INT NOT NULL REFERENCES roles(id),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE courses (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    credits INT NOT NULL CHECK (credits > 0),
    course_type VARCHAR(50) NOT NULL,
    language VARCHAR(50) NOT NULL,
    intended_major VARCHAR(255) NOT NULL,
    intended_year_of_study INT NOT NULL DEFAULT 0
);

CREATE TABLE course_registrations (
    id BIGSERIAL PRIMARY KEY,
    student_id VARCHAR(50) NOT NULL REFERENCES users(id),
    course_id VARCHAR(50) NOT NULL REFERENCES courses(id),
    status VARCHAR(30) NOT NULL,
    rejection_reason TEXT,
    requested_at TIMESTAMP NOT NULL,
    resolved_at TIMESTAMP
);

CREATE TABLE enrollments (
    id BIGSERIAL PRIMARY KEY,
    student_id VARCHAR(50) NOT NULL REFERENCES users(id),
    course_id VARCHAR(50) NOT NULL REFERENCES courses(id),
    UNIQUE(student_id, course_id)
);

CREATE TABLE marks (
    id BIGSERIAL PRIMARY KEY,
    student_id VARCHAR(50) NOT NULL REFERENCES users(id),
    course_id VARCHAR(50) NOT NULL REFERENCES courses(id),
    attestation1 NUMERIC(5,2) NOT NULL,
    attestation2 NUMERIC(5,2) NOT NULL,
    final_exam NUMERIC(5,2) NOT NULL,
    total NUMERIC(5,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE research_papers (
    id BIGSERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    journal TEXT NOT NULL,
    school VARCHAR(255) NOT NULL,
    pages INT NOT NULL,
    citations INT NOT NULL DEFAULT 0,
    doi VARCHAR(255),
    published_date DATE NOT NULL
);

CREATE TABLE researcher_papers (
    researcher_id VARCHAR(50) NOT NULL REFERENCES users(id),
    paper_id BIGINT NOT NULL REFERENCES research_papers(id),
    PRIMARY KEY (researcher_id, paper_id)
);

CREATE TABLE news (
    id BIGSERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    content TEXT NOT NULL,
    topic VARCHAR(50) NOT NULL,
    pinned BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE comments (
    id BIGSERIAL PRIMARY KEY,
    news_id BIGINT NOT NULL REFERENCES news(id),
    author_id VARCHAR(50) NOT NULL REFERENCES users(id),
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,
    sender_id VARCHAR(50) NOT NULL REFERENCES users(id),
    receiver_id VARCHAR(50) NOT NULL REFERENCES users(id),
    text TEXT NOT NULL,
    status VARCHAR(20) NOT NULL,
    sent_at TIMESTAMP NOT NULL
);

CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    actor_login VARCHAR(100) NOT NULL,
    action VARCHAR(100) NOT NULL,
    entity VARCHAR(100) NOT NULL,
    details TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_course_registrations_student ON course_registrations(student_id);
CREATE INDEX idx_course_registrations_status ON course_registrations(status);
CREATE INDEX idx_marks_student_course ON marks(student_id, course_id);
CREATE INDEX idx_messages_receiver_status ON messages(receiver_id, status);
CREATE INDEX idx_audit_logs_actor_created ON audit_logs(actor_login, created_at);
