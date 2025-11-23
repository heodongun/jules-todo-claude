'use client';

import { useState } from 'react';
import useSWR from 'swr';
import { postApi } from '@/lib/api';
import { Post } from '@/types/post';

export default function Home() {
  const [page, setPage] = useState(1);
  const { data, error, mutate } = useSWR(['posts', page], () => postApi.getAll(page));

  const [isCreating, setIsCreating] = useState(false);
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [author, setAuthor] = useState('');

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await postApi.create({ title, content, author });
      setTitle('');
      setContent('');
      setAuthor('');
      setIsCreating(false);
      mutate();
    } catch (error) {
      console.error('Failed to create post:', error);
      alert('Failed to create post');
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm('Are you sure you want to delete this post?')) return;
    try {
      await postApi.delete(id);
      mutate();
    } catch (error) {
      console.error('Failed to delete post:', error);
      alert('Failed to delete post');
    }
  };

  if (error) return <div className="error">Failed to load posts</div>;
  if (!data) return <div className="loading">Loading...</div>;

  return (
    <main className="container">
      <h1>Board Application</h1>

      <button onClick={() => setIsCreating(!isCreating)} className="btn-primary">
        {isCreating ? 'Cancel' : 'New Post'}
      </button>

      {isCreating && (
        <form onSubmit={handleCreate} className="post-form">
          <input
            type="text"
            placeholder="Title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
          />
          <textarea
            placeholder="Content"
            value={content}
            onChange={(e) => setContent(e.target.value)}
            required
          />
          <input
            type="text"
            placeholder="Author"
            value={author}
            onChange={(e) => setAuthor(e.target.value)}
            required
          />
          <button type="submit" className="btn-primary">Create</button>
        </form>
      )}

      <div className="posts-list">
        {data.posts.map((post: Post) => (
          <div key={post.id} className="post-card">
            <h2>{post.title}</h2>
            <p className="author">By {post.author}</p>
            <p className="content">{post.content}</p>
            <p className="date">
              Created: {new Date(post.createdAt).toLocaleString()}
            </p>
            <button onClick={() => handleDelete(post.id)} className="btn-danger">
              Delete
            </button>
          </div>
        ))}
      </div>

      <div className="pagination">
        <button
          onClick={() => setPage(page - 1)}
          disabled={page === 1}
          className="btn-secondary"
        >
          Previous
        </button>
        <span>Page {page} of {Math.ceil(data.total / data.pageSize)}</span>
        <button
          onClick={() => setPage(page + 1)}
          disabled={page >= Math.ceil(data.total / data.pageSize)}
          className="btn-secondary"
        >
          Next
        </button>
      </div>
    </main>
  );
}
