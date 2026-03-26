import { useEffect, useRef, useCallback } from 'react'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { Client, type IMessage } from '@stomp/stompjs'
import SockJS from 'sockjs-client'
import { useAuthStore } from '@/stores/authStore'
import * as chatApi from '../api/chatApi'
import type { ChatMessage, SendMessageRequest } from '../types'

const chatKeys = {
  threads: ['chat', 'threads'] as const,
  messages: (threadId: string) => ['chat', 'messages', threadId] as const,
}

export function useThreads() {
  return useQuery({
    queryKey: chatKeys.threads,
    queryFn: chatApi.getThreads,
  })
}

export function useMessages(threadId: string) {
  return useQuery({
    queryKey: chatKeys.messages(threadId),
    queryFn: () => chatApi.getMessages(threadId),
    enabled: !!threadId,
  })
}

export function useSendMessage(threadId: string) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (data: SendMessageRequest) => chatApi.sendMessage(threadId, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: chatKeys.messages(threadId) })
      queryClient.invalidateQueries({ queryKey: chatKeys.threads })
    },
  })
}

export function useOpenThread() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: ({ listingId, sellerId }: { listingId: string; sellerId: string }) =>
      chatApi.getOrCreateThread(listingId, sellerId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: chatKeys.threads })
    },
  })
}

export function useMarkRead(threadId: string) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: () => chatApi.markRead(threadId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: chatKeys.threads })
    },
  })
}

function useStompSocket(
  token: string | null,
  onMessage: (raw: string) => void,
) {
  const clientRef = useRef<Client | null>(null)
  const onMessageRef = useRef(onMessage)
  useEffect(() => {
    onMessageRef.current = onMessage
  })

  const connect = useCallback(() => {
    if (!token) return
    const client = new Client({
      webSocketFactory: () => new SockJS('/ws/chat'),
      connectHeaders: { Authorization: `Bearer ${token}` },
      onConnect: () => {
        client.subscribe('/user/queue/messages', (frame: IMessage) => {
          onMessageRef.current(frame.body)
        })
      },
      reconnectDelay: 5000,
    })
    client.activate()
    clientRef.current = client
  }, [token])

  useEffect(() => {
    connect()
    return () => { clientRef.current?.deactivate() }
  }, [connect])
}

/** Subscribe to real-time messages for a specific thread. */
export function useChatSocket(threadId: string, onMessage: (msg: ChatMessage) => void) {
  const token = useAuthStore((s) => s.token)
  useStompSocket(token, (raw) => {
    const msg = JSON.parse(raw) as ChatMessage
    if (msg.threadId === threadId) onMessage(msg)
  })
}

/** Keep the thread list fresh — invalidates on any incoming message. */
export function useThreadListSocket() {
  const token = useAuthStore((s) => s.token)
  const queryClient = useQueryClient()
  useStompSocket(token, () => {
    queryClient.invalidateQueries({ queryKey: chatKeys.threads })
  })
}
