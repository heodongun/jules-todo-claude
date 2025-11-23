import axios from 'axios';
import { Post, CreatePostRequest, UpdatePostRequest, PostListResponse } from '@/types/post';

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const postApi = {
  getAll: async (page: number = 1, pageSize: number = 10): Promise<PostListResponse> => {
    const response = await api.get<PostListResponse>(`/posts?page=${page}&pageSize=${pageSize}`);
    return response.data;
  },

  getById: async (id: number): Promise<Post> => {
    const response = await api.get<Post>(`/posts/${id}`);
    return response.data;
  },

  create: async (data: CreatePostRequest): Promise<Post> => {
    const response = await api.post<Post>('/posts', data);
    return response.data;
  },

  update: async (id: number, data: UpdatePostRequest): Promise<Post> => {
    const response = await api.put<Post>(`/posts/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/posts/${id}`);
  },
};
