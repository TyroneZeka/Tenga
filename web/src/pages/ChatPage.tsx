import { useParams, Link, useNavigate } from 'react-router-dom'
import { useState, useEffect, useRef } from 'react'
import { ArrowLeft, Smile } from 'lucide-react'
import { useThreads, useMessages, useSendMessage, useMarkRead, useChatSocket } from '@/features/chat'
import type { ChatMessage } from '@/features/chat'
import { useListing } from '@/features/listings'
import { useAuthStore } from '@/stores/authStore'
import { useQueryClient } from '@tanstack/react-query'

function formatTime(iso: string) {
  return new Date(iso).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
}

function shouldShowTimestamp(prev: ChatMessage | undefined, curr: ChatMessage) {
  if (!prev) return true
  return new Date(curr.sentAt).getTime() - new Date(prev.sentAt).getTime() > 5 * 60 * 1000
}

export default function ChatPage() {
  const { threadId } = useParams<{ threadId: string }>()
  const navigate = useNavigate()
  const user = useAuthStore((s) => s.user)
  const { data: threads } = useThreads()
  const thread = threads?.find((t) => t.id === threadId)

  const { data, isLoading } = useMessages(threadId!)
  const { mutate: sendMessage, isPending } = useSendMessage(threadId!)
  const { mutate: markRead } = useMarkRead(threadId!)
  const { data: listing } = useListing(thread?.listingId)
  const [text, setText] = useState('')
  const bottomRef = useRef<HTMLDivElement>(null)
  const queryClient = useQueryClient()
  const markedReadRef = useRef(false)
  const inputRef = useRef<HTMLInputElement>(null)

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
    inputRef.current?.focus()
  }

  const isBuyer = thread?.buyerId === user?.id
  const otherId = isBuyer ? thread?.sellerId : thread?.buyerId
  const maskedId = otherId ? otherId.slice(0, 3) + '***' + otherId.slice(-3) : '...'
  const currencyLabel = listing?.currency === 'USD' ? 'US$' : 'ZiG'
  const price = listing ? Number(listing.price).toLocaleString() : ''

  return (
    <div className="flex h-[calc(100vh-56px)] flex-col bg-[#f5f5f5]">
      {/* ── Header ────────────────────────────────────────────────── */}
      <header className="flex flex-shrink-0 items-center gap-3 border-b border-gray-200 bg-white px-3 py-2.5">
        <button
          onClick={() => navigate(-1)}
          className="flex h-8 w-8 items-center justify-center rounded-full text-gray-500 hover:bg-gray-100"
        >
          <ArrowLeft className="h-5 w-5" />
        </button>
        <div className="min-w-0 flex-1">
          <p className="truncate text-sm font-semibold text-gray-900">
            {thread?.listingTitle ?? 'Chat'}
          </p>
          <p className="text-[11px] text-gray-400">{maskedId}</p>
        </div>
      </header>

      {/* ── Listing card ──────────────────────────────────────────── */}
      {listing && (
        <Link
          to={`/listings/${listing.id}`}
          className="flex flex-shrink-0 items-center gap-3 border-b border-gray-200 bg-white px-4 py-2.5 hover:bg-gray-50"
        >
          {listing.imageUrls[0] ? (
            <img
              src={listing.imageUrls[0]}
              alt={listing.title}
              className="h-14 w-14 flex-shrink-0 rounded-lg object-cover"
            />
          ) : (
            <div className="flex h-14 w-14 flex-shrink-0 items-center justify-center rounded-lg bg-gray-100 text-xl">
              🏷️
            </div>
          )}
          <div className="min-w-0 flex-1">
            <p className="truncate text-xs text-gray-600">{listing.title}</p>
            <p className="mt-0.5 text-base font-bold text-[#ff6000]">
              {currencyLabel} {price}
            </p>
            {listing.city && <p className="text-[10px] text-gray-400">{listing.city}</p>}
          </div>
        </Link>
      )}
      {!listing && thread && (
        <div className="flex flex-shrink-0 items-center gap-3 border-b border-gray-200 bg-white px-4 py-2.5">
          <div className="h-14 w-14 flex-shrink-0 animate-pulse rounded-lg bg-gray-200" />
          <div className="flex-1 space-y-1.5">
            <div className="h-3 w-40 animate-pulse rounded bg-gray-200" />
            <div className="h-4 w-24 animate-pulse rounded bg-gray-200" />
          </div>
        </div>
      )}

      {/* ── Messages ──────────────────────────────────────────────── */}
      <div className="flex-1 overflow-y-auto px-4 py-3">
        {isLoading && (
          <p className="py-8 text-center text-sm text-gray-400">Loading messages…</p>
        )}

        {allMessages.map((msg, i) => {
          const isMine = msg.senderId === user?.id
          const prevMsg = allMessages[i - 1]
          const showTime = shouldShowTimestamp(prevMsg, msg)
          const avatarSeed = otherId ? otherId.charCodeAt(0) % 50 : 2

          return (
            <div key={msg.id}>
              {showTime && (
                <p className="my-3 text-center text-[11px] text-gray-400">
                  {formatTime(msg.sentAt)}
                </p>
              )}

              <div
                className={`mb-1.5 flex items-end gap-2 ${isMine ? 'flex-row-reverse' : 'flex-row'}`}
              >
                {/* Avatar for other party */}
                {!isMine && (
                  <img
                    src={`https://loremflickr.com/32/32/portrait?lock=${avatarSeed}`}
                    alt="avatar"
                    className="mb-1 h-8 w-8 flex-shrink-0 rounded-full object-cover"
                    onError={(e) => {
                      e.currentTarget.style.display = 'none'
                    }}
                  />
                )}

                {/* Bubble */}
                <div className={`flex max-w-[72%] flex-col ${isMine ? 'items-end' : 'items-start'}`}>
                  <div
                    className={`rounded-2xl px-3.5 py-2 text-sm leading-relaxed ${
                      isMine
                        ? 'rounded-br-sm bg-[#ff6000] text-white'
                        : 'rounded-bl-sm bg-white text-gray-900 shadow-sm'
                    }`}
                  >
                    {msg.body}
                  </div>
                  {isMine && msg.status === 'READ' && (
                    <span className="mt-0.5 text-[10px] text-gray-400">Read</span>
                  )}
                </div>
              </div>
            </div>
          )
        })}

        <div ref={bottomRef} />
      </div>

      {/* ── Input bar ─────────────────────────────────────────────── */}
      <form
        onSubmit={handleSend}
        className="flex flex-shrink-0 items-center gap-2 border-t border-gray-200 bg-white px-3 py-2.5"
      >
        <button
          type="button"
          className="flex h-9 w-9 flex-shrink-0 items-center justify-center rounded-full text-gray-400 hover:bg-gray-100"
        >
          <Smile className="h-5 w-5" />
        </button>

        <input
          ref={inputRef}
          value={text}
          onChange={(e) => setText(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === 'Enter' && !e.shiftKey) {
              e.preventDefault()
              handleSend(e)
            }
          }}
          className="flex-1 rounded-full border border-gray-200 bg-gray-50 px-4 py-2 text-sm text-gray-800 placeholder:text-gray-400 focus:border-[#ff6000] focus:outline-none focus:ring-1 focus:ring-[#ff6000]"
          placeholder="Type a message, press Enter to send"
        />

        <button
          type="submit"
          disabled={isPending || !text.trim()}
          className="flex-shrink-0 rounded-full bg-[#ff6000] px-4 py-2 text-sm font-semibold text-white hover:bg-[#e55500] disabled:opacity-40"
        >
          Send
        </button>
      </form>
    </div>
  )
}
