import { useNavigate } from 'react-router-dom'
import { ArrowLeft, ShoppingBag, CheckCircle, Clock, XCircle, RefreshCw } from 'lucide-react'
import { useTransactions } from '@/features/payment'
import type { Transaction } from '@/features/payment'
import { useAuthStore } from '@/stores/authStore'

function statusIcon(status: Transaction['status']) {
  switch (status) {
    case 'COMPLETED':
      return <CheckCircle className="h-4 w-4 text-green-500" />
    case 'PENDING':
    case 'PROCESSING':
      return <Clock className="h-4 w-4 text-amber-500" />
    case 'FAILED':
      return <XCircle className="h-4 w-4 text-red-500" />
    case 'REFUNDED':
      return <RefreshCw className="h-4 w-4 text-blue-500" />
  }
}

function statusLabel(status: Transaction['status']) {
  switch (status) {
    case 'COMPLETED':
      return 'Completed'
    case 'PENDING':
      return 'Pending'
    case 'PROCESSING':
      return 'Processing'
    case 'FAILED':
      return 'Failed'
    case 'REFUNDED':
      return 'Refunded'
  }
}

function statusColor(status: Transaction['status']) {
  switch (status) {
    case 'COMPLETED':
      return 'text-green-600 bg-green-50'
    case 'PENDING':
    case 'PROCESSING':
      return 'text-amber-600 bg-amber-50'
    case 'FAILED':
      return 'text-red-600 bg-red-50'
    case 'REFUNDED':
      return 'text-blue-600 bg-blue-50'
  }
}

export default function OrdersPage() {
  const navigate = useNavigate()
  const user = useAuthStore((s) => s.user)!
  const { data, isLoading } = useTransactions()

  const transactions = data?.content ?? []

  return (
    <div className="min-h-screen bg-[#f5f5f5] pb-20">
      {/* Header */}
      <header className="sticky top-0 z-10 flex items-center gap-3 border-b border-gray-200 bg-white px-4 py-3">
        <button
          onClick={() => navigate(-1)}
          className="flex h-8 w-8 items-center justify-center rounded-full text-gray-500 hover:bg-gray-100"
        >
          <ArrowLeft className="h-5 w-5" />
        </button>
        <h1 className="text-base font-bold text-gray-900">My Orders</h1>
      </header>

      {/* Loading */}
      {isLoading && (
        <div className="space-y-2 p-4">
          {Array.from({ length: 4 }).map((_, i) => (
            <div key={i} className="h-20 animate-pulse rounded-xl bg-gray-200" />
          ))}
        </div>
      )}

      {/* Empty */}
      {!isLoading && transactions.length === 0 && (
        <div className="flex flex-col items-center justify-center py-24 text-gray-400">
          <ShoppingBag className="mb-3 h-12 w-12 text-gray-200" />
          <p className="text-sm font-medium">No orders yet</p>
          <p className="mt-1 text-xs text-gray-300">Your payment history will appear here</p>
        </div>
      )}

      {/* Transaction list */}
      {transactions.length > 0 && (
        <div className="space-y-2 p-4">
          {transactions.map((tx) => {
            const isBuyer = tx.buyerId === user.id
            const currencyLabel = tx.currency === 'USD' ? 'US$' : 'ZiG'
            const amount = Number(tx.amount).toLocaleString()
            const date = new Date(tx.createdAt).toLocaleDateString([], {
              day: 'numeric',
              month: 'short',
              year: 'numeric',
            })

            return (
              <div key={tx.id} className="rounded-xl bg-white p-4 shadow-sm">
                <div className="flex items-start justify-between gap-3">
                  <div className="flex min-w-0 flex-1 flex-col">
                    <div className="flex items-center gap-2">
                      <span
                        className={`inline-flex items-center gap-1 rounded-full px-2 py-0.5 text-[11px] font-semibold ${statusColor(tx.status)}`}
                      >
                        {statusIcon(tx.status)}
                        {statusLabel(tx.status)}
                      </span>
                      <span className="text-[11px] text-gray-400">
                        {isBuyer ? 'Purchase' : 'Sale'}
                      </span>
                    </div>

                    <p className="mt-1.5 text-sm font-semibold text-gray-900">
                      {tx.paymentMethod === 'ECOCASH' ? 'EcoCash' : 'InnBucks'}
                    </p>
                    <p className="mt-0.5 text-[11px] text-gray-400">
                      {date}
                      {tx.gatewayReference && (
                        <> · Ref: {tx.gatewayReference}</>
                      )}
                    </p>
                  </div>

                  <div className="flex-shrink-0 text-right">
                    <p className={`text-base font-bold ${isBuyer ? 'text-red-500' : 'text-green-600'}`}>
                      {isBuyer ? '-' : '+'}{currencyLabel} {amount}
                    </p>
                  </div>
                </div>
              </div>
            )
          })}
        </div>
      )}
    </div>
  )
}
