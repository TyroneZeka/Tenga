import { useParams, Link } from 'react-router-dom'
import { useState, useEffect, useRef } from 'react'
import { Send, ArrowLeft } from 'lucide-react'
import { useMessages, useSendMessage, useMarkRead, useChatSocket } from '@/features/chat'
import type { ChatMessage } from '@/features/chat'
import { useAuthStore } from '@/stores/authStore'
import { useQueryClient } from '@tanstack/react-query'

export default function ChatPage() {
  const { threadId } = useParams<{ threadId: string }>()
  const user = useAuthStore((s) => s.user)
  const { data, isLoading } = useMessages(threadId!)
  const { mutate: sendMessage, isPending } = useSendMessage(threadId!)
  const { mutate: markRead } = useMarkRead(threadId!)
  const [text, setText] = useState('')
  const bottomRef = useRef<HTMLDivElement>(null)
  const queryClient = useQueryClient()
  const markedReadRef = useRef(false)

  const [extraMessages, setExtraMessages] = useState<ChatMessage[]>([])

  const serverMessages = data?.messages ?? []
  const allMessages = [
    ...serverMessages,
    ...extraMessages.filter((e) => !serverMessages.some((s) => s.id === e.id)),
  ]

  useEffect(() => {
    if (data && !markedReadRef.current) {
      markedReadRef.current = true
      markRead()
    }
  }, [data, markRead])

  useChatSocket(threadId!, (msg) => {
    setExtraMessages((prev) => {
      if (prev.some((m) => m.id === msg.id)) return prev
      return [...prev, msg]
    })
    queryClient.invalidateQueries({ queryKey: ['chat', 'threads'] })
  })

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [allMessages.length])

  function handleSend(e: React.FormEvent) {
    e.preventDefault()
    if (!text.trim()) return
    const body = text.trim()
    setText('')
    sendMessage({ body })
  }

  return (
    <div className="flex h-[calc(100vh-4rem)] flex-col bg-gray-50">
      <header className="flex items-center gap-3 border-b border-gray-200 bg-white px-4 py-3">
        <Link to="/chat" className="text-gray-500 hover:text-gray-800">
          <ArrowLeft className="h-5 w-5" />
        </Link>
        <h1 className="text-base font-semibold text-gray-900">Chat</h1>
      </header>

      <div className="flex-1 space-y-2 overflow-y-auto px-4 py-4">
        {isLoading && <p className="text-center text-sm text-gray-400">Loading…</p>}
        {allMessages.map((msg) => {
          const isMine = msg.senderId === user?.id
          return (
            <div key={msg.id} className={`flex ${isMine ? 'justify-end' : 'justify-start'}`}>
              <div
                className={`max-w-xs rounded-2xl px-3.5 py-2 text-sm ${
                  isMine
                    ? 'rounded-br-sm bg-primary-500 text-white'
                    : 'rounded-bl-sm bg-white text-gray-900 shadow-sm'
                }`}
              >
                {msg.body}
              </div>
            </div>
          )
        })}
        <div ref={bottomRef} />
      </div>

      <form
        onSubmit={handleSend}
        className="flex items-center gap-2 border-t border-gray-200 bg-white px-4 py-3"
      >
        <input
          value={text}
          onChange={(e) => setText(e.target.value)}
          className="flex-1 rounded-full border border-gray-200 bg-gray-50 px-4 py-2 text-sm focus:border-primary-400 focus:outline-none focus:ring-1 focus:ring-primary-400"
          placeholder="Message…"
        />
        <button
          type="submit"
          disabled={isPending || !text.trim()}
          className="flex h-9 w-9 items-center justify-center rounded-full bg-primary-500 text-white hover:bg-primary-600 disabled:opacity-40"
        >
          <Send className="h-4 w-4" />
        </button>
      </form>
    </div>
  )
}
