--create the tags table
CREATE TABLE tags
(
    id INT generated always as identity primary key,
    name VARCHAR(255) NOT NULL
);

--create the user_tags join table
CREATE TABLE user_tags
(
  user_id bigint NOT NULL,
  tag_id Int NOT NULL,
  PRIMARY KEY (user_id, tag_id),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
);