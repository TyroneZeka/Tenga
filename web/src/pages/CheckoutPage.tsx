import { useState } from 'react'
import { useParams, useNavigate, Link } from 'react-router-dom'
import { ArrowLeft, CheckCircle } from 'lucide-react'
import { useListing } from '@/features/listings'
import { useInitiatePayment } from '@/features/payment'
import type { PaymentMethod } from '@/features/payment'

export default function CheckoutPage() {
  const { listingId } = useParams<{ listingId: string }>()
  const { data: listing } = useListing(listingId!)
  const { mutate, isPending, data: transaction, error } = useInitiatePayment()
  const [method, setMethod] = useState<PaymentMethod>('ECOCASH')
  const navigate = useNavigate()

  if (!listing) return null

  const currencyLabel = listing.currency === 'USD' ? 'US$' : 'ZiG'

  if (transaction) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-gray-50 px-4">
        <div className="w-full max-w-sm rounded-2xl bg-white p-8 shadow-md text-center">
          <CheckCircle className="mx-auto mb-3 h-12 w-12 text-green-500" />
          <h2 className="text-lg font-semibold text-gray-900">Payment initiated</h2>
          <p className="mt-1 text-sm text-gray-500">
            {method === 'ECOCASH'
              ? 'Check your phone for the EcoCash USSD prompt.'
              : 'Approve the payment in your InnBucks app.'}
          </p>
          <p className="mt-3 text-xs text-gray-400">Ref: {transaction.gatewayReference ?? transaction.id}</p>
          <button
            onClick={() => navigate('/')}
            className="mt-5 w-full rounded-lg bg-gray-900 py-2.5 text-sm font-medium text-white hover:bg-gray-800"
          >
            Back to home
          </button>
        </div>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="mx-auto max-w-md px-4 py-6">
        <Link
          to={`/listings/${listingId}`}
          className="mb-4 flex items-center gap-1 text-sm text-gray-500 hover:text-gray-800"
        >
          <ArrowLeft className="h-4 w-4" /> Back
        </Link>

        <h1 className="mb-5 text-xl font-semibold text-gray-900">Checkout</h1>

        <div className="rounded-xl bg-white p-5 shadow-sm">
          <div className="mb-4 border-b border-gray-100 pb-4">
            <p className="text-sm text-gray-600">{listing.title}</p>
            <p className="mt-1 text-xl font-bold text-primary-600">
              {currencyLabel} {listing.price.toLocaleString()}
            </p>
          </div>

          <p className="mb-2 text-sm font-medium text-gray-700">Payment method</p>
          <div className="space-y-2">
            {(['ECOCASH', 'INNBUCKS'] as PaymentMethod[]).map((m) => (
              <label
                key={m}
                className={`flex cursor-pointer items-center gap-3 rounded-lg border p-3 transition ${
                  method === m ? 'border-primary-500 bg-primary-50' : 'border-gray-200'
                }`}
              >
                <input
                  type="radio"
                  name="method"
                  value={m}
                  checked={method === m}
                  onChange={() => setMethod(m)}
                  className="accent-primary-500"
                />
                <span className="text-sm font-medium">{m === 'ECOCASH' ? 'EcoCash' : 'InnBucks'}</span>
              </label>
            ))}
          </div>

          {error && (
            <p className="mt-3 text-sm text-red-600">
              {(error as { response?: { data?: { message?: string } } }).response?.data?.message ??
                'Payment failed. Please try again.'}
            </p>
          )}

          <button
            disabled={isPending}
            onClick={() =>
              mutate({
                listingId: listingId!,
                method,
                amount: listing.price,
                currency: listing.currency,
              })
            }
            className="mt-5 w-full rounded-lg bg-primary-500 py-2.5 text-sm font-medium text-white hover:bg-primary-600 disabled:opacity-50"
          >
            {isPending ? 'Processing…' : `Pay ${currencyLabel} ${listing.price.toLocaleString()}`}
          </button>
        </div>
      </div>
    </div>
  )
}
