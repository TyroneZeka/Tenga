import { api } from '@/lib/api'
import type { ChatMessage, ChatThread, MessagePage, SendMessageRequest } from '../types'

export async function getThreads(): Promise<ChatThread[]> {
  const res = await api.get('/chat/threads')
  return res.data
}

export async function getOrCreateThread(listingId: string, sellerId: string): Promise<ChatThread> {
  const res = await api.post('/chat/threads', { listingId, sellerId })
  return res.data
}

export async function getMessages(threadId: string, cursor?: string): Promise<MessagePage> {
  const res = await api.get(`/chat/threads/${threadId}/messages`, {
    params: cursor ? { cursor } : undefined,
  })
  return res.data
}

export async function sendMessage(
  threadId: string,
  data: SendMessageRequest,
): Promise<ChatMessage> {
  const res = await api.post(`/chat/threads/${threadId}/messages`, { ...data, type: 'TEXT' })
  return res.data
}

export async function markRead(threadId: string): Promise<void> {
  await api.patch(`/chat/threads/${threadId}/read`)
}
